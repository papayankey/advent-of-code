let read_file filename = failwith "Todo"
let parse_input input = failwith "Todo"

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
      | [] -> -1, 1
      | hd :: tl ->
        (match String.index_opt hd '^' with
         | None -> aux (row + 1) col tl
         | Some n -> row, n)
    in
    aux 0 0 grid
  ;;
end

let is_obstacle grid r c = grid.(r).[c] = '#'

type direction =
  | Up
  | Down
  | Right
  | Left

module Part1 = struct
  let get_distinct pos =
    let rec aux acc = function
      | [] -> acc
      | ((_, _) as hd) :: tl ->
        if not (List.mem hd acc) then aux (hd :: acc) tl else aux acc tl
    in
    aux [] pos
  ;;

  let get_positions grid sr sc =
    let rows, cols = Grid.dimension grid in
    let rec aux r c dir acc =
      if r = 0 || c = 0 || r = rows - 1 || c = cols - 1
      then acc @ [ r, c ]
      else (
        match dir with
        | Up ->
          if is_obstacle grid (r - 1) c
          then aux r c Right acc
          else aux (r - 1) c dir acc @ [ r, c ]
        | Right ->
          if is_obstacle grid r (c + 1)
          then aux r c Down acc
          else aux r (c + 1) dir acc @ [ r, c ]
        | Down ->
          if is_obstacle grid (r + 1) c
          then aux r c Left acc
          else aux (r + 1) c dir acc @ [ r, c ]
        | Left ->
          if is_obstacle grid r (c - 1)
          then aux r c Up acc
          else aux r (c - 1) dir acc @ [ r, c ])
    in
    aux sr sc Up [ sr, sc ]
  ;;

  let solve () =
    let grid = Grid.make "" in
    let sr, sc = Grid.get_start (Array.to_list grid) in
    let total_distinct_pos = get_positions grid sr sc |> get_distinct |> List.length in
    Printf.printf "Part1: %d" total_distinct_pos
  ;;
end

module Part2 = struct end

let () = print_endline "day 6"
