interface GameState {
  currentPhase: String;
  currentPlayerID: number;
  currentWorkerID: number;
  boardInfo: BoardInfo | null;
  playersInfo: PlayerInfo[] | null;
  end: boolean;
  winner: String;
  lastSuccess: boolean;
}

interface Grid {
  worker: Worker | null;
  workerID: number;
  workerPlayerID: number;
  Building: Building | null;
  selectedAsTarget: boolean;
  selectedAsSource: boolean;
}

interface BoardInfo{
  x: number;
  y: number;
  buildings: Building[];
}

interface Building{
  x: number;
  y: number;
  units: BuildingUnit[];
}

interface BuildingUnit{
  info: String;
}

interface PlayerInfo{
  workers: Worker[];
}

interface Worker{
  x: number;
  y: number;
}


export type { GameState, Grid }