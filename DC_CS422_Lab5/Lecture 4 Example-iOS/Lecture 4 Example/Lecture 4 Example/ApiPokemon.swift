//
//  ApiPokemon.swift
//  Lecture 4 Example
//
//  Created by Andrew Taylor on 2/3/23.
//

import Foundation

struct ApiPokemonContainer: Decodable {
    let results: Array<ApiPokemon>
}

struct ApiPokemon: Decodable {
    let name: String
    let url: String
}
