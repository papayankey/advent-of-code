let read_file filename =
  let ic = open_in filename in
  In_channel.input_all ic
;;

module Grid = struct
  type t = string array

  let make input : t = Array.of_list (String.split_on_char '\n' input)

  let dimension grid =
    let rows = Array.length grid
    and cols = String.length grid.(0) in
    rows, cols
  ;;

  let get_start grid =
    let rec aux row col = function
      | [] -> -1, -1
      | hd :: tl ->
        (match String.index_opt hd '^' with
         | None -> aux (row + 1) col tl
         | Some n -> row, n)
    in
    aux 0 0 grid
  ;;

  let is_within_bounds r c grid =
    let rows, cols = dimension grid in
    r >= 0 && r < rows && c >= 0 && c < cols
  ;;

  let is_obstacle grid r c = grid.(r).[c] = '#'

  type direction =
    | Up
    | Down
    | Right
    | Left

  let get_next_position ~row ~col dir =
    match dir with
    | Up -> Some (row - 1, col)
    | Down -> Some (row + 1, col)
    | Left -> Some (row, col - 1)
    | Right -> Some (row, col + 1)
  ;;

  let change_direction dir =
    match dir with
    | Up -> Right
    | Down -> Left
    | Left -> Up
    | Right -> Down
  ;;
end

module Part1 = struct
  let get_distinct pos =
    let rec aux acc = function
      | [] -> acc
      | ((_, _) as hd) :: tl ->
        if not (List.mem hd acc) then aux (hd :: acc) tl else aux acc tl
    in
    aux [] pos
  ;;

  let get_visited_positions grid sr sc dir =
    let rec aux row col grid dir visited =
      match Grid.get_next_position ~row ~col dir with
      | None -> visited
      | Some (r, c) ->
        if not (Grid.is_within_bounds r c grid)
        then visited
        else if Grid.is_obstacle grid r c
        then aux row col grid (Grid.change_direction dir) visited
        else aux r c grid dir ((r, c) :: visited)
    in
    aux sr sc grid dir [ sr, sc ]
  ;;

  let solve input =
    let grid = Grid.make input in
    let start_row, start_col = Grid.get_start (Array.to_list grid)
    and start_dir = Grid.Up in
    get_visited_positions grid start_row start_col start_dir
    |> get_distinct
    |> List.length
  ;;
end

module Part2 = struct end

let () = read_file "data/test.txt" |> Part1.solve |> Printf.printf "Part1: %d\n"
