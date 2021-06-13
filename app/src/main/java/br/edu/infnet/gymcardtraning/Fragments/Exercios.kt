package br.edu.infnet.gymcardtraning.Fragments

import android.os.Bundle
import android.provider.CalendarContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.edu.infnet.gymcardtraning.R


class Exercios : Fragment() {

      override fun onCreateView(
          inflater: LayoutInflater,container: ViewGroup?,
          saveInstances: Bundle?
      ): View?{
          return inflater.inflate(R.layout.fragment_exercios, container, false)
      }
}