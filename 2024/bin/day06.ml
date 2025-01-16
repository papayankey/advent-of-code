let read_file filename =
  let ic = open_in filename in
  In_channel.input_all ic |> String.trim
;;

module Grid = struct
  type cell =
    | Empty
    | Obstacle
    | Guard

  type t = cell list list

  type direction =
    | Up
    | Down
    | Right
    | Left

  let cell_of_char = function
    | '.' -> Empty
    | '#' -> Obstacle
    | _ -> Guard
  ;;

  let make input : t =
    let string_list = String.split_on_char '\n' input in
    List.map
      (fun s -> List.init (String.length s) (fun i -> cell_of_char s.[i]))
      string_list
  ;;

  let dimensions grid =
    let rows = List.length grid
    and cols = List.length (List.nth grid 0) in
    rows, cols
  ;;

  let is_within_bounds r c grid =
    let rows, cols = dimensions grid in
    r >= 0 && r < rows && c >= 0 && c < cols
  ;;

  let update_grid r c grid cell =
    List.mapi
      (fun i row ->
        if i = r then List.mapi (fun j col -> if j = c then cell else col) row else row)
      grid
  ;;

  let is_obstacle grid r c = List.nth (List.nth grid r) c = Obstacle

  let next_position ~row ~col grid dir =
    let r, c =
      match dir with
      | Up -> row - 1, col
      | Down -> row + 1, col
      | Left -> row, col - 1
      | Right -> row, col + 1
    in
    if is_within_bounds r c grid then Some (r, c) else None
  ;;

  let change_direction direction =
    match direction with
    | Up -> Right
    | Down -> Left
    | Left -> Up
    | Right -> Down
  ;;

  module Point = struct
    type t =
      { row : int
      ; col : int
      }

    let compare = compare
  end

  module PointSet = Set.Make (Point)
end

let get_guard_start grid =
  let open Grid in
  let rec found_guard row col =
    match row with
    | [] -> false, -1
    | hd :: tl -> if hd = Guard then true, col else found_guard tl (col + 1)
  in
  let rec aux row col (grid : Grid.t) : Point.t =
    match grid with
    | [] -> { row = -1; col = -1 }
    | hd :: tl ->
      (match found_guard hd 0 with
       | true, col -> { row; col }
       | false, _ -> aux (row + 1) col tl)
  in
  aux 0 0 grid
;;

module Part1 = struct
  let get_visited_positions grid sr sc dir =
    let rec aux row col grid dir visited =
      match Grid.next_position ~row ~col grid dir with
      | None -> visited
      | Some (r, c) ->
        if Grid.is_obstacle grid r c
        then aux row col grid (Grid.change_direction dir) visited
        else aux r c grid dir (Grid.PointSet.add { row = r; col = c } visited)
    in
    aux sr sc grid dir Grid.PointSet.(empty |> add { row = sr; col = sc })
  ;;

  let solve input =
    let grid = Grid.make input in
    let start_point = get_guard_start grid in
    let start_dir = Grid.Up in
    get_visited_positions grid start_point.row start_point.col start_dir
    |> Grid.PointSet.elements
    |> List.length
  ;;
end

module Part2 = struct
  module Point = struct
    type t =
      { row : int
      ; col : int
      ; dir : Grid.direction
      }

    let compare p1 p2 =
      let c = compare p1.row p2.row in
      if c <> 0
      then c
      else (
        let c = compare p1.col p2.col in
        if c <> 0 then c else compare p1.dir p2.dir)
    ;;
  end

  module PointSet = Set.Make (Point)

  let get_visited_positions grid sr sc dir =
    let rec aux row col grid dir visited =
      match Grid.next_position ~row ~col grid dir with
      | None -> visited
      | Some (r, c) ->
        if Grid.is_obstacle grid r c
        then aux row col grid (Grid.change_direction dir) visited
        else aux r c grid dir (PointSet.add { row = r; col = c; dir } visited)
    in
    aux sr sc grid dir PointSet.(empty |> add { row = sr; col = sc; dir })
  ;;

  let rec creates_loop g row col dir visited =
    match Grid.next_position ~row ~col g dir with
    | None -> false
    | Some (r, c) ->
      if PointSet.mem { row = r; col = c; dir } visited
      then true
      else if Grid.is_obstacle g r c
      then creates_loop g row col (Grid.change_direction dir) visited
      else (
        let new_visited = PointSet.add { row = r; col = c; dir } visited in
        creates_loop g r c dir new_visited)
  ;;

  let count_loops_with_obstructions grid sr sc dir =
    let visited_positions = get_visited_positions grid sr sc dir in
    let rec count_loops visited_list placed_positions loop_count =
      match visited_list with
      | [] -> loop_count
      | (pos : Point.t) :: tl ->
        if Grid.PointSet.mem { row = pos.row; col = pos.col } placed_positions
        then count_loops tl placed_positions loop_count
        else (
          let placed_positions =
            Grid.PointSet.add { row = pos.row; col = pos.col } placed_positions
          in
          let updated_grid = Grid.update_grid pos.row pos.col grid Grid.Obstacle in
          if creates_loop updated_grid sr sc dir PointSet.empty
          then count_loops tl placed_positions (loop_count + 1)
          else count_loops tl placed_positions loop_count)
    in
    count_loops (PointSet.elements visited_positions) Grid.PointSet.empty 0
  ;;

  let solve input =
    let grid = Grid.make input in
    let start_point = get_guard_start grid in
    let start_dir = Grid.Up in
    count_loops_with_obstructions grid start_point.row start_point.col start_dir
  ;;
end

let () = read_file "data/prod.txt" |> Part1.solve |> Printf.printf "Part1: %d\n"
let () = read_file "data/prod.txt" |> Part2.solve |> Printf.printf "Part2: %d\n"
