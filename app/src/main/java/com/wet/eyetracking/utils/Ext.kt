package com.wet.eyetracking.utils

import com.wet.eyetracking.model.ExperimentItem
import io.realm.Realm
import java.io.File

private const val MIME_IMAGE = "image/png"

//File
fun File.isImage(): Boolean {
    return isFile && MyUtils.getMimeType(absolutePath) == MIME_IMAGE
}

fun File.getImagesSorted(): List<File> {
    val files = ArrayList<File>()

    if (name.contains(MyUtils.DETECTED_EYES_DIR)) {

    } else if (isDirectory) {
        val sorted = listFiles().sortedWith(compareBy {
            Integer.parseInt(it.name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
        })

        for (f in sorted) {
            files.addAll(f.getImagesSorted())
        }

    } else if (isImage()) {
        files.add(this)
    }

    return files
}


//Realm
fun Realm.saveEyeExperiment(experiment: ExperimentItem) {
    beginTransaction()
    insertOrUpdate(experiment)
    commitTransaction()
}