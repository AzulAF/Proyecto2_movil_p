package com.azul.proyecto2.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azul.proyecto2.databinding.CharacterElementBinding
import com.azul.proyecto2.model.Character
import com.bumptech.glide.Glide

class CharactersAdapter (private var characters: ArrayList<Character>, private var onCharacterClicked: (Character) -> Unit): RecyclerView.Adapter<CharactersAdapter.ViewHolder>(){
    class ViewHolder(private var binding: CharacterElementBinding): RecyclerView.ViewHolder(binding.root){
            fun bind(character: Character){
                //Nombre y apellidos
                binding.tvName.text = character.fullName

                //Imagen
                Glide.with(itemView.context).load(character.imageUrl).into(binding.ivPhoto)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CharacterElementBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = characters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(characters[position])

        holder.itemView.setOnClickListener {
            onCharacterClicked(characters[position])
        }

    }


}