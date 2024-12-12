open Core

let read_file (filename : string) =
  let ic = In_channel.create filename in
  In_channel.input_all ic
;;

let populate_hash line dict =
  let k, v = Scanf.sscanf line "%d|%d" (fun l r -> l, r) in
  Hashtbl.update dict k ~f:(function
    | None -> [ v ]
    | Some s -> v :: s)
;;

let get_rules (data : string) =
  let dict : (int, int list) Hashtbl.t = Hashtbl.create (module Int) in
  let lines = String.split_lines data in
  List.iter ~f:(fun line -> populate_hash line dict) lines;
  dict
;;

let get_pages (data : string) =
  String.split_lines data
  |> List.map ~f:(fun line ->
    Array.of_list (List.map ~f:int_of_string (String.split ~on:',' line)))
  |> Array.of_list
;;

let parse_input (data : string) =
  let pat = {|\n\n|} in
  let re = Re.compile (Re.Pcre.re pat) in
  let res = Re.split re data in
  let rules = get_rules (Stdlib.List.nth res 0) in
  let pages = get_pages (Stdlib.List.nth res 1) in
  rules, pages
;;

module Part1 = struct
  let get_valid_ordered_pages rules pages =
    let res = ref [] in
    for i = 0 to Array.length pages - 1 do
      let p = pages.(i) in
      let len = Array.length p in
      for j = 0 to len - 1 do
        for k = j + 1 to len - (j + 1) do
          if Hashtbl.mem rules p.(j) && Array.exists p ~f:(fun v -> v = p.(k))
          then 
            res := !res @ [ List.of_array pages.(i) ];
        done
      done
    done;
    !res
  ;;

  let solve (input : string) =
    (* print_endline ""; *)
    let rules, pages = parse_input input in
    let res = get_valid_ordered_pages rules pages in
    List.iter
      ~f:(fun data ->
        Printf.printf "[%s]\n" (String.concat ~sep:", " (List.map ~f:Int.to_string data)))
      res
  ;;
  (* Hashtbl.iteri rules ~f:(fun ~key ~data ->
     Printf.printf
     "%d: [%s]\n"
     key
     (String.concat ~sep:", " (List.map ~f:Int.to_string data)));
     print_endline "============";
     List.iter
     ~f:(fun data ->
     Printf.printf "[%s]\n" (String.concat ~sep:", " (List.map ~f:Int.to_string data)))
     pages
     ;; *)
end

module Part2 = struct end

let () = read_file "data/test.txt" |> Part1.solve
