let read_file filename =
  let open Core in
  let ic = In_channel.create filename in
  In_channel.input_all ic
;;

let get_rules input =
  let rule_lookup = Hashtbl.create 10 in
  let lines = String.split_on_char '\n' input in
  List.iter
    (fun line ->
      let k, v = Scanf.sscanf line "%d|%d" (fun l r -> l, r) in
      Hashtbl.add rule_lookup k v)
    lines;
  rule_lookup
;;

let get_pages input =
  Array.of_list (String.split_on_char '\n' input)
  |> Array.map (fun line ->
    String.split_on_char ',' line |> List.map int_of_string |> Array.of_list)
;;

let parse_input input =
  let pat = {|\n\n|} in
  let res = Re.split (Re.compile (Re.Pcre.re pat)) (String.trim input) in
  let rule_lookup = get_rules (List.nth res 0) in
  let updates = get_pages (List.nth res 1) in
  rule_lookup, updates
;;

exception Invalid_ordering

let sum_middle_page updates =
  updates
  |> List.map (fun update -> List.nth update (List.length update / 2))
  |> List.fold_left ( + ) 0
;;

let is_valid_update rule_lookup update =
  let is_valid = ref true
  and len = Array.length update in
  (try
     for j = 0 to len - 2 do
       let curr_page = update.(j) in
       if Hashtbl.mem rule_lookup curr_page
       then (
         let curr_page_rules = Hashtbl.find_all rule_lookup curr_page in
         for k = j + 1 to len - 1 do
           let next = update.(k) in
           if not (List.mem next curr_page_rules) then raise Invalid_ordering
         done)
       else raise Invalid_ordering
     done
   with
   | Invalid_ordering -> is_valid := false);
  !is_valid
;;

let get_ordered_updates rule_lookup updates =
  let ordered_updates = ref [] in
  Array.iteri
    (fun i _ ->
      let update = updates.(i) in
      if is_valid_update rule_lookup update
      then ordered_updates := Array.to_list update :: !ordered_updates)
    updates;
  List.rev !ordered_updates
;;

module Part1 = struct
  let solve (input : string) =
    let rules, pages = parse_input input in
    get_ordered_updates rules pages |> sum_middle_page |> Printf.printf "%d\n"
  ;;
end

module Part2 = struct
  let get_unordered_updates valid_updates updates =
    Core.Array.filter updates ~f:(fun update ->
      not (List.mem (Core.List.of_array update) valid_updates))
  ;;

  let reorder_invalid_update rule_lookup update =
    Array.stable_sort
      (fun a b ->
        let assoc = Hashtbl.find_all rule_lookup a in
        if List.mem b assoc then 0 else 1)
      update;
    update
  ;;

  let solve (input : string) =
    let rule_lookup, updates = parse_input input in
    let valid_updates = get_ordered_updates rule_lookup updates in
    let reordered_updates =
      get_unordered_updates valid_updates updates
      |> Array.map (reorder_invalid_update rule_lookup)
      |> Array.map Core.List.of_array
      |> Core.List.of_array
    in
    sum_middle_page reordered_updates |> Printf.printf "%d\n"
  ;;
end

let () = read_file "data/prod.txt" |> Part1.solve
let () = read_file "data/prod.txt" |> Part2.solve
