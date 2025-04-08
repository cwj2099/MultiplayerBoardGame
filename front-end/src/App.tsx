import React from 'react';
import './App.css'; // import the css file to enable your styles.
import { GameState, Grid } from './game';

/**
 * Define the type of the props field for a React component
 */
interface Props { }

/**
 * Using generics to specify the type of props and state.
 * props and state is a special field in a React component.
 * React will keep track of the value of props and state.
 * Any time there's a change to their values, React will
 * automatically update (not fully re-render) the HTML needed.
 * 
 * props and state are similar in the sense that they manage
 * the data of this component. A change to their values will
 * cause the view (HTML) to change accordingly.
 * 
 * Usually, props is passed and changed by the parent component;
 * state is the internal value of the component and managed by
 * the component itself.
 */
class App extends React.Component<Props, GameState> {
  private initialized: boolean = false;
  private godList: string[] = [];
  private god: string = "";

  //The following variables represents user's current selection
  private x1: number = -1;
  private y1: number = -1;
  private x2: number = -1;
  private y2: number = -1;
  /**
   * @param props has type Props
   */
  constructor(props: Props) {
    super(props)
    /**
     * state has type GameState as specified in the class inheritance.
     */
    this.state = { 
      currentPlayerID: -1, 
      currentWorkerID: -1,
      currentPhase: "",
      boardInfo: null,
      playersInfo: null, 
      end: false, 
      winner: "",
      lastSuccess: true
    }
  }

  /**
   * Use arrow function, i.e., () => {} to create an async function,
   * otherwise, 'this' would become undefined in runtime. This is
   * just an issue of Javascript.
   */
  newGame = async () => {
    const response = await fetch('/newGame');
    await this.updateState(response);
  }

  newGodGame = async () => {
    const response = await fetch('/newGodGame');
    await this.updateState(response);
  }


  update = async () =>{
    const response = await fetch('/update');
    await this.updateState(response);
  }

  /**
   * play will generate an anonymous function that the component
   * can bind with.
   * @param x 
   * @param y 
   * @returns 
   */
  play = async () => {
    const response = await fetch(`/play?x1=${this.x1}&y1=${this.y1}&x2=${this.x2}&y2=${this.y2}`)
    await this.updateState(response);
  }
  
  special = async (content:string) =>{
    const response = await fetch('/special?content=' + content)
    await this.updateState(response);
  }

  private async updateState(response: Response) {
    const json = await response.json()
    this.setState({
      currentPhase: json['currentPhase'],
      currentPlayerID: json['currentPlayerID'],
      currentWorkerID: json['currentWorkerID'],
      boardInfo: json['boardInfo'],
      playersInfo: json['playersInfo'],
      lastSuccess: json['ActionSuccess']
    });

    this.x1 = -1;
    this.y1 = -1;

    const playersInfo = json['playersInfo']
    if(playersInfo !== null){
      this.x2 = playersInfo[json['currentPlayerID']].workers[json['currentWorkerID']].x;
      this.y2 = playersInfo[json['currentPlayerID']].workers[json['currentWorkerID']].y;
    }

    if(json['GameMode'] === 'God'){
      const response2 = await fetch('/getGod')
      const json2 = await response2.json()
      this.godList = json2['GodList']
      
      const response3 = await fetch('/currentGod')
      const json3 = await response3.json()
      this.god = json3['CurrentGod']
    }
    else{
      this.godList =[];
      this.god = "";
    }
    this.setState({})
  }

  createGrid(): React.ReactNode {
    const boardInfo = this.state.boardInfo
    const playersInfo = this.state.playersInfo 
    if(boardInfo == null) return <></>
    if(playersInfo == null) return <></>

    //Intialize grids
    const grids: Grid[][] = new Array(boardInfo.x)
    .fill(null) // Initialize rows
    .map(() =>
      new Array(boardInfo.y).fill(null).map(() => ({
        worker: null,
        workerID: -1, // Default value for no worker
        workerPlayerID: -1, // Default value for no player
        Building: null, // Default value for no building
        selectedAsTarget: false,
        selectedAsSource: false
      }))
    );

    //update grids information
    playersInfo.forEach((player)=>{
      player.workers.forEach((worker)=>{
        if(worker.x >=0 && worker.y >=0){
          const grid = grids[worker.x][worker.y]
          grid.worker = worker;
          grid.workerID = player.workers.indexOf(worker)
          grid.workerPlayerID = playersInfo.indexOf(player)
        }
      })
    })

    boardInfo.buildings.forEach((building)=>{
      const grid = grids[building.x][building.y]
      grid.Building = building;
    })
    
    //render grids
    let gridDisplay: React.ReactNode[] = [];

    for(let i = 0; i< grids.length; i++){
      for(let j = 0; j< grids[i].length; j++){
        const grid = grids[i][j]
        grid.selectedAsTarget = this.x1 === i && this.y1 ===j;
        grid.selectedAsSource = this.x2 === i && this.y2 ===j;

        let gridClassName = `cell `;
        if(grid.selectedAsSource || grid.selectedAsTarget){
          gridClassName = this.state.currentPlayerID === 0 ? 'cell player1' : 'cell player2'
        }

        let workerContent
        if(grid.worker !== null){
          let style;
          if(grid.Building !== null) style = {top: (75 - (grid.Building.units.length + 1)*20)+'%'};
          if(grid.workerPlayerID === 1) {workerContent = <img className="worker" src="/blueMinion.png" style={style} alt="blueWorker" /> }
          else workerContent = <img className="worker" src="/redMinion.png" style={style} alt="orangeWorker"/>
        }

        let blockContent: React.ReactNode[] = [];
        if(grid.Building !== null){
          for(let w = 0; w < grid.Building.units.length; w ++){
            let info = grid.Building.units.at(w)?.info
            const img = info === "Dome"? "dome.png" : "block.png"
            const style =  {top: (75 - w*20)+'%'};
            blockContent.push(<img className="block" src={img} style={style}/>)
          }
        }

        gridDisplay.push(
          <div key ={`${i}-${j}` }>
            <div className={gridClassName} onClick={() => this.onGridClicked(i,j,grid)}>
              {blockContent}
              {workerContent}
            </div>
          </div>
        );
      }
    } 

    return <div id="board">{gridDisplay}</div>;
  }

  onGridClicked(x:number, y: number, grid: Grid){
    console.log( x + "-" + y + " is clicked")
    //case no worker selected
    if(grid.worker == null || grid.workerPlayerID != this.state.currentPlayerID){
      this.x1 = x;
      this.y1 = y;
      console.log( x + "-" + y + " is set as target position")
    }
    else{
      this.x2 = x;
      this.y2 = y;
      console.log( x + "-" + y + " is set as source position")
    }

    this.setState({})
  }



  createInstruction(): React.ReactNode{
    var toReturn = <></>
    const playerID = this.state.currentPlayerID+1
    const workerID = this.state.currentWorkerID+1
    var style ={background: "linear-gradient(135deg, #BF360C, ##fc5c2c)"}

    if(playerID === 1) style ={background: "linear-gradient(135deg, #FFE082, #FFD54F)"}
    if(playerID === 2) style ={background: "linear-gradient(135deg, #B3E5FC, #81D4FA)"}

    switch(this.state.currentPhase){
      case 'initialization':
        console.log('Phase is initialization');
        toReturn = <div id="instructions" style={style}>
          Player {playerID} put your worker {workerID}</div>
        break;
        
      case 'end':
        console.log('Phase is end');
        toReturn = <div id="instructions" style={style}>
          Player {playerID} wins!</div>
        break;
        
      case 'move':
        console.log('Phase is move');
        toReturn = <div id="instructions" style={style}>
          Player {playerID} pick a worker and set it's destination</div>
        break;
        
      case 'build':
        console.log('Phase is build');
        toReturn = <div id="instructions" style={style}>
          Player {playerID} build a block around that worker</div>
        break;
      
      case 'pickGodCard':
        console.log('Phase is CardPick');
        toReturn = <div id="instructions" style={style}>
          Player {playerID} Pick your god card</div>
        break;

      case 'DemeterBuild':
        console.log('Phase is DemeterBuild');
        toReturn = <div id="instructions" style={style}>
          Player {playerID} Build at two different place at most twice, can skip second build</div>
        break;
      
      case 'HephaestusBuild':
        console.log('Phase is HephaestusBuild');
        toReturn = <div id="instructions" style={style}>
          Player {playerID} Build at same place at most twice, can skip second build</div>
        break;

      case 'MinotaurMove':
        console.log('Phase is MinotaurMove');
        toReturn = <div id="instructions" style={style}>
          Player {playerID} pick a worker and set it's destination. You can push opponent's worker away if possible</div>
        break;
      
      case 'PanMove' :
        console.log('Phase is PanMove');
        toReturn = <div id="instructions" style={style}>
          Player {playerID} pick a worker and set it's destination.If the destination is 2 level lower than original place, you win</div>
        break;

      default:
        console.log('Game not started');
        toReturn = <div id="instructions" style={style}>
          game not started, press start</div>
        break;
    }
    return toReturn
  }

  getGodCard(): React.ReactNode{

    let cards: React.ReactNode[] = [];

    if(this.state.currentPhase === 'pickGodCard'){
      this.godList.forEach((card:string)=>{
        cards.push(
          <button onClick={()=>{this.special(card)}}>{card}</button>
        )
      })
    }
    else{
      cards.push(
        <div>Current God is: {this.god}</div>
      )

      if(this.god === 'Demeter' || this.god === 'Hephaestus'){
        cards.push(
        <button onClick={()=>{this.special('skip')}}>Skip</button>
        )
      }
    }

    return <div className="side-panel">
          <h2>God Cards</h2>
          {cards}
    </div> 
  }

  /**
   * This function will call after the HTML is rendered.
   * We update the initial state by creating a new game.
   * @see https://reactjs.org/docs/react-component.html#componentdidmount
   */
  componentDidMount(): void {
    /**
     * setState in DidMount() will cause it to render twice which may cause
     * this function to be invoked twice. Use initialized to avoid that.
     */
    if (!this.initialized) {
      this.update();
      this.initialized = true;
    }
  }

  /**
   * The only method you must define in a React.Component subclass.
   * @returns the React element via JSX.
   * @see https://reactjs.org/docs/react-component.html
   */
  render(): React.ReactNode {
    /**
     * We use JSX to define the template. An advantage of JSX is that you
     * can treat HTML elements as code.
     * @see https://reactjs.org/docs/introducing-jsx.html
     */

    if(this.state.currentPhase === ''){
      
    }
    return (
      <div>
        {this.getGodCard()}
        {this.createGrid()}
        {this.createInstruction()}
        <div id="instructions">{this.state.lastSuccess?"":"That action was not valid"}</div>
        <div id="bottombar">
          <button onClick={this.newGame}>New Game</button>
          <button onClick={this.newGodGame}>New God Game</button>
          <button onClick={this.play}>Submit</button>
        </div>
      </div>
    );
  }
}

export default App;
