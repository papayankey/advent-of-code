open Core

let read_file filename =
  let ic = In_channel.create filename in
  In_channel.input_lines ic
;;

let tuple_of_lines (input : string list) =
  List.map ~f:(fun line -> Scanf.sscanf line "%d   %d" (fun l r -> l, r)) input
  |> List.unzip
;;

module Part1 = struct
  let solve (input : string list) : int =
    let left, right = tuple_of_lines input in
    let sorted_left = List.sort left ~compare:Int.compare
    and sorted_right = List.sort right ~compare:Int.compare in
    let result =
      List.fold2 ~init:0 ~f:(fun acc l r -> acc + abs (l - r)) sorted_left sorted_right
    in
    match result with
    | Base.List.Or_unequal_lengths.Ok sum -> sum
    | Base.List.Or_unequal_lengths.Unequal_lengths ->
      failwith "List are of unequal length"
  ;;
end

module Part2 = struct
  let freq_count (list : int list) (n : int) = List.count ~f:(fun r -> n = r) list

  let solve (input : string list) : int =
    let left, right = tuple_of_lines input in
    List.fold ~init:0 ~f:(fun acc cur -> acc + (freq_count right cur * cur)) left
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
