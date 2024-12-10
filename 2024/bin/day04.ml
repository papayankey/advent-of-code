open Core
open Poly

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

let grid_dimension grid =
  let rows = Array.length grid
  and cols = Array.length grid.(0) in
  rows, cols
;;

let chars_to_string grid = Array.map ~f:(fun a -> Array.map ~f:Char.to_string a) grid

module Part1 = struct
  let count_xmas x y grid =
    let word = "XMAS" in
    let rows, cols = grid_dimension grid in
    let top =
      if x - 3 >= 0
      then
        if grid.(x).(y) ^ grid.(x - 1).(y) ^ grid.(x - 2).(y) ^ grid.(x - 3).(y) = word
        then 1
        else 0
      else 0
    and bottom =
      if x + 3 < rows
      then
        if grid.(x).(y) ^ grid.(x + 1).(y) ^ grid.(x + 2).(y) ^ grid.(x + 3).(y) = word
        then 1
        else 0
      else 0
    and left =
      if y - 3 >= 0
      then
        if grid.(x).(y) ^ grid.(x).(y - 1) ^ grid.(x).(y - 2) ^ grid.(x).(y - 3) = word
        then 1
        else 0
      else 0
    and right =
      if y + 3 < cols
      then
        if grid.(x).(y) ^ grid.(x).(y + 1) ^ grid.(x).(y + 2) ^ grid.(x).(y + 3) = word
        then 1
        else 0
      else 0
    and top_left =
      if x - 3 >= 0 && y - 3 >= 0
      then
        if grid.(x).(y)
           ^ grid.(x - 1).(y - 1)
           ^ grid.(x - 2).(y - 2)
           ^ grid.(x - 3).(y - 3)
           = word
        then 1
        else 0
      else 0
    and top_right =
      if x - 3 >= 0 && y + 3 < cols
      then
        if grid.(x).(y)
           ^ grid.(x - 1).(y + 1)
           ^ grid.(x - 2).(y + 2)
           ^ grid.(x - 3).(y + 3)
           = word
        then 1
        else 0
      else 0
    and bottom_left =
      if x + 3 < rows && y - 3 >= 0
      then
        if grid.(x).(y)
           ^ grid.(x + 1).(y - 1)
           ^ grid.(x + 2).(y - 2)
           ^ grid.(x + 3).(y - 3)
           = word
        then 1
        else 0
      else 0
    and bottom_right =
      if x + 3 < rows && y + 3 < cols
      then
        if grid.(x).(y)
           ^ grid.(x + 1).(y + 1)
           ^ grid.(x + 2).(y + 2)
           ^ grid.(x + 3).(y + 3)
           = word
        then 1
        else 0
      else 0
    in
    top + bottom + left + right + top_left + top_right + bottom_left + bottom_right
  ;;

  let count_xmases grid =
    let rows, cols = grid_dimension grid in
    let rec aux x y acc =
      if x < rows
      then
        if y < cols
        then
          if grid.(x).(y) = 'X'
          then aux x (y + 1) (acc + count_xmas x y (chars_to_string grid))
          else aux x (y + 1) acc
        else aux (x + 1) 0 acc
      else acc
    in
    aux 0 0 0
  ;;

  let solve (filename : string) = create_grid filename |> count_xmases
end

module Part2 = struct
  let count_x_mas x y grid =
    let mas = "MAS"
    and sam = "SAM" in
    let rows, cols = grid_dimension grid in
    if x - 1 >= 0 && x + 1 < rows && y - 1 >= 0 && y + 1 < cols
    then (
      let left_diag = grid.(x - 1).(y - 1) ^ grid.(x).(y) ^ grid.(x + 1).(y + 1) in
      let right_diag = grid.(x + 1).(y - 1) ^ grid.(x).(y) ^ grid.(x - 1).(y + 1) in
      if (left_diag = mas || left_diag = sam) && (right_diag = mas || right_diag = sam)
      then 1
      else 0)
    else 0
  ;;

  let count_x_mases grid =
    let rows, cols = grid_dimension grid in
    let rec aux x y acc =
      if x < rows
      then
        if y < cols
        then
          if grid.(x).(y) = 'A'
          then aux x (y + 1) (acc + count_x_mas x y (chars_to_string grid))
          else aux x (y + 1) acc
        else aux (x + 1) 0 acc
      else acc
    in
    aux 0 0 0
  ;;

  let solve (filename : string) = create_grid filename |> count_x_mases
end

let () =
  let count = Part1.solve "data/prod.txt" in
  Printf.printf "%d\n" count
;;

let () =
  let count = Part2.solve "data/prod.txt" in
  Printf.printf "%d\n" count
;;
