open Aoc2024.Util

let () =
  set_session ();
  let year =
    print_endline "Input advent year: ";
    read_int ()
  and day =
    print_endline "Input advent day: ";
    read_int ()
  in
  let file_path = "data/prod.txt" in
  Lwt_main.run (get_day_input year day file_path)
;;
