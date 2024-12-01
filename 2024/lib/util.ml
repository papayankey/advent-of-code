open Lwt.Infix
open Cohttp
open Cohttp_lwt_unix

let get_url year day = Format.sprintf "https://adventofcode.com/%d/day/%d/input" year day

let ensure_path_exists dir =
  if not (Sys.file_exists dir && Sys.is_directory dir) then Unix.mkdir dir 0o755
;;

let write_to_file file_path content =
  Lwt_io.with_file ~mode:Lwt_io.Output file_path (fun out_channel ->
    Lwt_io.write out_channel content)
;;

let set_session () =
  print_endline "Please add the session id for advent of code account: ";
  let value = read_line () in
  Unix.putenv "ADVENT_SESSION" value
;;

let get_day_input year day file_path =
  let session_env =
    match Sys.getenv_opt "ADVENT_SESSION" with
    | Some value -> if value = "" then failwith "Invalid advent session" else value
    | None -> failwith "Session id is required"
  in
  let dir = Filename.dirname file_path in
  ensure_path_exists dir;
  let session_id = "session=" ^ session_env
  and url = get_url year day in
  let headers = Header.init () |> fun h -> Header.add h "Cookie" session_id in
  Client.get ~headers (Uri.of_string url)
  >>= fun (response, body) ->
  let status = Response.status response |> Code.string_of_status in
  Cohttp_lwt.Body.to_string body
  >>= fun body_string ->
  write_to_file file_path body_string >>= fun () -> Lwt_io.printf "Status: %s\n" status
;;
