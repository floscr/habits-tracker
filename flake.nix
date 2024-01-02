{
  description = "lawn.fun";
  inputs.nixpkgs-unstable.url = "nixpkgs/nixos-unstable";
  inputs.nixpkgs.url = "nixpkgs/master";
  inputs.flake-utils.url = "github:numtide/flake-utils";
  inputs.flake-compat = {
    url = "github:edolstra/flake-compat";
    flake = false;
  };

  outputs = { self, nixpkgs, nixpkgs-unstable, flake-utils, ... }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;
          config = { allowUnfree = true; };
        };
        pkgs-unstable = import nixpkgs-unstable {
          inherit system;
          config = { allowUnfree = true; };
        };
      in
      {
        devShell = pkgs.mkShell rec {
          buildInputs = with pkgs; [
            babashka
            clojure
          ];

          shellHook = ''
          '';
        };
      });

}
