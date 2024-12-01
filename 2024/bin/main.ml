open Aoc2024.Util

let () =
  set_session ();
  let file_path = "data/prod.txt" in
  Lwt_main.run (get_day_input 2023 1 file_path)
;;
