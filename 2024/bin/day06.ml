let read_file filename =
  let ic = open_in filename in
  In_channel.input_all ic
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

  type point =
    { row : int
    ; col : int
    }

  let char_to_cell = function
    | '.' -> Empty
    | '#' -> Obstacle
    | _ -> Guard
  ;;

  let make input : t =
    let string_list = String.split_on_char '\n' input in
    List.map
      (fun s -> List.init (String.length s) (fun i -> char_to_cell s.[i]))
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

  let point_to_string point = Printf.printf "(%d, %d)" point.row point.col
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
end

let get_guard_start grid =
  let open Grid in
  let rec found_guard row col =
    match row with
    | [] -> false, -1
    | hd :: tl -> if hd = Guard then true, col else found_guard tl (col + 1)
  in
  let rec aux row col (grid : Grid.t) =
    match grid with
    | [] -> { row = -1; col = -1 }
    | hd :: tl ->
      (match found_guard hd 0 with
       | true, col -> { row; col }
       | false, _ -> aux (row + 1) col tl)
  in
  aux 0 0 grid
;;

let get_visited_positions grid sr sc dir =
  let open Grid in
  let rec aux row col grid dir visited =
    match next_position ~row ~col grid dir with
    | None -> visited
    | Some (r, c) ->
      (match is_obstacle grid r c with
       | true -> aux row col grid (change_direction dir) visited
       | false -> aux r c grid dir ({ row = r; col = c } :: visited))
  in
  aux sr sc grid dir [ { row = sr; col = sc } ]
;;

module Part1 = struct
  let get_distinct_positions (pos : Grid.point list) =
    let rec aux acc pos =
      match pos with
      | [] -> acc
      | (_ as hd) :: tl ->
        if not (List.mem hd acc) then aux (hd :: acc) tl else aux acc tl
    in
    aux [] pos
  ;;

  let solve input =
    let grid = Grid.make input in
    let start_point = get_guard_start grid in
    let start_dir = Grid.Up in
    let res =
      get_visited_positions grid start_point.row start_point.col start_dir
      |> get_distinct_positions
    in
    List.iter Grid.point_to_string res;
    List.length res
  ;;
end

(*
   module Part2 = struct
  type guard_state =
    | Loop
    | Got_away

  module Point = struct
    type t =
      { row : int
      ; column : int
      ; direction : Grid.direction
      }

    let compare = compare
  end

  module PointSet = Set.Make (Point)

  let _solve input = failwith "todo"
end
*)

let () = read_file "data/test.txt" |> Part1.solve |> Printf.printf "Part1: %d\n"

(*
   let () =
   let _ = read_file "data/test.txt" |> Part2.solve in
   ()
   ;;
*)
