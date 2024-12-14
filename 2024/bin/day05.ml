let read_file filename =
  let open Core in
  let ic = In_channel.create filename in
  In_channel.input_all ic
;;

let get_rules input =
  let tbl = Hashtbl.create 10 in
  let lines = String.split_on_char '\n' input in
  List.iter
    (fun line ->
      let k, v = Scanf.sscanf line "%d|%d" (fun l r -> l, r) in
      Hashtbl.add tbl k v)
    lines;
  tbl
;;

let get_pages input =
  Array.of_list (String.split_on_char '\n' input)
  |> Array.map (fun line ->
    String.split_on_char ',' line |> List.map int_of_string |> Array.of_list)
;;

let parse_input input =
  let pat = {|\n\n|} in
  let res = Re.split (Re.compile (Re.Pcre.re pat)) (String.trim input) in
  let tbl = get_rules (List.nth res 0) in
  let pages = get_pages (List.nth res 1) in
  tbl, pages
;;

module Part1 = struct
  exception Invalid_ordering

  let sum_middle_page_number data =
    data
    |> List.map (fun d ->
      let mid = List.length d / 2 in
      List.nth d mid)
    |> List.fold_left ( + ) 0
  ;;

  let check_valid_ordering tbl is_valid item =
    let len = Array.length item in
    try
      for j = 0 to len - 2 do
        let curr = item.(j) in
        if Hashtbl.mem tbl curr
        then (
          let assoc = Hashtbl.find_all tbl curr in
          for k = j + 1 to len - 1 do
            let next = item.(k) in
            if not (List.mem next assoc) then raise Invalid_ordering
          done)
        else raise Invalid_ordering
      done
    with
    | Invalid_ordering -> is_valid := false
  ;;

  let get_valid_ordered tbl pages =
    let res = ref [] in
    for i = 0 to Array.length pages - 1 do
      let is_valid = ref true in
      let item = pages.(i) in
      check_valid_ordering tbl is_valid item;
      if !is_valid then res := Array.to_list item :: !res
    done;
    List.rev !res
  ;;

  let solve (input : string) =
    let rules, pages = parse_input input in
    get_valid_ordered rules pages |> sum_middle_page_number |> Printf.printf "%d\n"
  ;;
end

module Part2 = struct end

let () = read_file "data/prod.txt" |> Part1.solve
