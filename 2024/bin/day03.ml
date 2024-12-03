open Core
open Poly

type mul_state =
  | Enable
  | Disable

type instr =
  | Mul of int * int
  | Do
  | Dont
[@@deriving show]

let read_file (filename : string) = In_channel.read_all filename
let extract_digits instr = Scanf.sscanf instr "mul(%d,%d)" (fun l r -> l, r)

let build_instrs (sub : string) =
  match String.slice sub 0 3 with
  | "mul" ->
    let p = extract_digits sub in
    Mul (fst p, snd p)
  | "don" -> Dont
  | _ -> Do
;;

let parse_input (input : string) =
  let regex = Re.compile (Re.Pcre.re {|mul\(\d+,\d+\)|do\(\)|don't\(\)|}) in
  Re.all regex input
  |> List.map ~f:(fun subs ->
    let sub = Re.Group.get subs 0 in
    build_instrs sub)
;;

module Part1 = struct
  let solve (input : string) =
    input
    |> parse_input
    |> List.fold ~init:0 ~f:(fun acc instr ->
      let res =
        match instr with
        | Mul (a, b) -> a * b
        | Do | Dont -> 0
      in
      acc + res)
  ;;
end

module Part2 = struct
  let solve (input : string) =
    let state = ref Enable in
    input
    |> parse_input
    |> List.fold ~init:0 ~f:(fun acc instr ->
      let res =
        match instr with
        | Mul (a, b) -> if !state = Enable then a * b else 0
        | Do ->
          state := Enable;
          0
        | Dont ->
          state := Disable;
          0
      in
      acc + res)
  ;;
end

let () =
  let ans = read_file "data/prod.txt" |> Part1.solve in
  Printf.printf "%d\n" ans
;;

let () =
  let ans = read_file "data/prod.txt" |> Part2.solve in
  Printf.printf "%d\n" ans
;;
