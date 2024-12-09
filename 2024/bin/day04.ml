open Core

let create_grid (filename : string) =
  let ic = Stdlib.open_in filename in
  let rec read_lines acc =
    try
      let line = Stdlib.input_line ic in
      let row = Stdlib.(Array.of_seq String.(to_seq (trim line))) in
      read_lines (row :: acc)
    with
    | End_of_file ->
      In_channel.close ic;
      Array.of_list_rev acc
  in
  read_lines []
;;

let find_all (line : string) =
  let open Re in
  let compiled_pat = compile (Pcre.re "XMAS|SAMX") in
  let rec aux acc pos =
    if pos >= String.length line
    then List.rev acc
    else (
      match exec ~pos compiled_pat line with
      | exception Stdlib.Not_found -> List.rev acc
      | group ->
        let matched = Group.get group 0 in
        aux (matched :: acc) (Group.start group 0 + 1))
  in
  List.length (aux [] 0)
;;

let dimensions grid =
  let rows = Array.length grid
  and cols = Array.length grid.(0) in
  rows, cols
;;

let count_horizontal grid =
  let rows, cols = dimensions grid in
  let rec aux grid x y acc =
    if x < rows
    then
      if y < cols
      then aux grid x (y + 1) (acc @ [ grid.(x).(y) ])
      else aux grid (x + 1) 0 acc
    else acc
  in
  find_all (String.of_list (aux grid 0 0 []))
;;

let count_vertical grid =
  let rows, cols = dimensions grid in
  let rec aux grid x y acc =
    if y < cols
    then
      if x < rows
      then aux grid (x + 1) y (acc @ [ grid.(x).(y) ])
      else aux grid 0 (y + 1) acc
    else acc
  in
  find_all (String.of_list (aux grid 0 0 []))
;;

let rec read_main_diagonal x y grid =
  let rows, cols = dimensions grid in
  let rec aux x y acc =
    if x < rows && y < cols then aux (x + 1) (y + 1) (acc @ [ grid.(x).(y) ]) else acc
  in
  aux 0 0 []
;;

let count_diagonal grid =
  let rows, cols = dimensions grid in
  let rec collect_diagonals x y acc =
    if x < rows
    then (
      let res = read_main_diagonal x 0 grid in
      collect_diagonals (x + 1) y (acc @ [ res ]))
    else if y < cols
    then (
      let res = read_main_diagonal 0 y grid in
      collect_diagonals x (y + 1) (acc @ [ res ]))
    else acc
  in
  let values = collect_diagonals 0 0 [] in
  List.iter ~f:(fun v -> Printf.printf "%s\n" (String.of_char_list v)) values
;;

module Part1 = struct
  let solve (filename : string) =
    let grid = create_grid filename in
    count_horizontal grid + count_vertical grid
  ;;
end

module Part2 = struct end

let () =
  let res = Part1.solve "data/test.txt" in
  Printf.printf "%d\n" res
;;

let () =
  let grid = create_grid "data/test.txt" in
  let _ = count_diagonal grid in
  ()
;;
