package cs.mad.lecture4example

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cs.mad.lecture4example.databinding.PokemonItemBinding

class PokemonAdapter(pokemon: List<Pokemon>): RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {
    private val pokemon = pokemon.toMutableList()
    private val spriteUrl = ""

    class ViewHolder(val binding: PokemonItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(PokemonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = pokemon[position]

        holder.binding.name.text = data.name
        holder.binding.link.text = data.url

        val spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" +
                "${data.url.dropLast(1).substringAfterLast('/')}.png"
        Glide.with(holder.itemView.context)
            .load(spriteUrl)
            .centerCrop()
            .into(holder.binding.sprite)
    }

    override fun getItemCount() = pokemon.size

    fun update(all: List<Pokemon>) {
        pokemon.clear()
        pokemon.addAll(all)
        notifyDataSetChanged()
    }
}