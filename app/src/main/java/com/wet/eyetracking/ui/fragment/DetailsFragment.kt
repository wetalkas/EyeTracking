package com.wet.eyetracking.ui.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.Entry
import com.wet.eyetracking.model.ExperimentItem
import com.wet.eyetracking.R
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_details.*
import java.io.BufferedWriter
import java.io.FileNotFoundException
import java.io.FileWriter
import java.io.IOException
import java.util.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class DetailsFragment : Fragment() {

    internal var id: Long = 0

    companion object {

        private val ID = "id"

        fun newInstance(id: Long): DetailsFragment {
            val args = Bundle()
            args.putLong(ID, id)
            val fragment = DetailsFragment()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = arguments!!.getLong(ID)

        val entries = ArrayList<Entry>()
        val realm = Realm.getDefaultInstance()
        val experiment = realm.where(ExperimentItem::class.java).equalTo(ID, id).findFirst()
        val pointRealmObjects = experiment.points

        var text = ""

        Log.d("entry count", ": " + pointRealmObjects.size)



        for (i in pointRealmObjects.indices) {
            if (pointRealmObjects[i].getX() == -12345.0) {
                if (i > 0 && i < pointRealmObjects.size - 1) {
                    val old = pointRealmObjects[i - 1].getX()
                    val future = pointRealmObjects[i + 1].getX()

                    val oldY = pointRealmObjects[i - 1].getY()
                    val futureY = pointRealmObjects[i + 1].getY()

                    val x = ((old + future) / 2).toInt()
                    val y = ((oldY + futureY) / 2).toInt()

                    entries.add(Entry(x.toFloat(), y.toFloat()))

                    text += "${i + 1}.   x = $x,  y = $y         вычислено вручную \n"

                } else if (i == 0 && pointRealmObjects.size > 1) {
                    val x = pointRealmObjects[1].getX().toInt()
                    val y = pointRealmObjects[1].getY().toInt()

                    entries.add(Entry(x.toFloat(), y.toFloat()))

                    text += "${i + 1}.   x = $x,  y = $y         вычислено вручную \n"

                }

            } else {
                val x = pointRealmObjects[i].getX().toInt()
                val y = pointRealmObjects[i].getY().toInt()

                text += "${i + 1}.   x = $x,  y = $y \n"

                entries.add(Entry(x.toFloat(), y.toFloat()))
            }
        }

        tvCoordinates.text = text

        writeTextArrayToFile(text, false)

    }


    fun writeTextArrayToFile(textLines: String, append: Boolean): Boolean {
        var wroteOK = false
        try {
            var writer: BufferedWriter? = null
            try {
                /*
                 * if (append) it means add more lines to file
                 * if (!append) it means delete old contents of file
                 */
                writer = BufferedWriter(FileWriter("/storage/emulated/0/procc/coordinates.txt", append))
                /*
                 * Modern usage would be for (String s : textLines)
                 */
                writer.write(textLines)
                // end for
                /*
                 * When you have finished writing, you can return "true"
                 */
                wroteOK = true
            }//end inner try (might throw IOException or FileNotFoundException)

            finally {
                /*
                 * If there has been an exception, the writer might be null, so you must test for nullity.
                 */
                writer?.close()//end if
            }/*
             * This finally will definitely be executed, but might throw an IOException, so it must be inside a try
             *///end finally
        }//end outer try
        catch (fnf: FileNotFoundException) {
        }//end catch FNFE
        catch (ioe: IOException) {

        }
        //end catch IOE
        return wroteOK
    }//end method


}
