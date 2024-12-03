open Core
open Poly

let read_file (filename : string) =
  let ic = In_channel.create filename in
  In_channel.input_lines ic
;;

let parse_input (input : string list) =
  input
  |> List.map ~f:(fun line ->
    List.map ~f:(fun n -> int_of_string (String.strip n)) (String.split ~on:' ' line))
;;

let in_range a b = abs (a - b) >= 1 && abs (a - b) <= 3

let is_increasing (list : int list) =
  let rec loop list =
    match list with
    | [] | [ _ ] -> true
    | a :: (b :: _ as tl) -> a < b && in_range a b && loop tl
  in
  loop list
;;

let is_decreasing (list : int list) =
  let rec loop list =
    match list with
    | [] | [ _ ] -> true
    | a :: (b :: _ as tl) -> a > b && in_range a b && loop tl
  in
  loop list
;;

module Part1 = struct
  let solve (data : string list) =
    parse_input data
    |> List.fold ~init:0 ~f:(fun acc r ->
      if is_increasing r || is_decreasing r then acc + 1 else acc)
  ;;
end

module Part2 = struct
  (* Removes an element at index from the list *)
  let remove_at_index (list : int list) (i : int) =
    let rec loop j list =
      match list with
      | [] -> []
      | _ :: tl when i = j -> tl
      | hd :: tl -> hd :: loop (j + 1) tl
    in
    loop 0 list
  ;;

  (* Checks if removing one element can make the list ordered *)
  let can_be_ordered (list : int list) =
    let len = List.length list in
    List.exists
      ~f:(fun idx ->
        let lst = remove_at_index list idx in
        is_increasing lst || is_decreasing lst)
      (List.init len ~f:Fun.id)
  ;;

  let solve (data : string list) =
    parse_input data
    |> List.fold ~init:0 ~f:(fun acc r ->
      if is_increasing r || is_decreasing r || can_be_ordered r then acc + 1 else acc)
  ;;
end

let () =
  let ans = read_file "data/prod.txt" |> Part1.solve in
  Printf.printf "Part1: %d\n" ans
;;

let () =
  let ans = read_file "data/prod.txt" |> Part2.solve in
  Printf.printf "Part2: %d\n" ans
;;
