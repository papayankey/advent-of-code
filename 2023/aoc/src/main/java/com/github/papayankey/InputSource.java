package com.github.papayankey;

import java.nio.file.Path;

public class InputSource {
   private InputSource() {
      throw new IllegalStateException("Utility class");
   }

   public static Path load(String inputDirectory) {
      return Path.of("aoc", "src", "main", "java", "com", "github", "papayankey", inputDirectory).resolve("input.txt");
   }
}
