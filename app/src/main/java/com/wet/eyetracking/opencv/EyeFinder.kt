package com.wet.eyetracking.opencv

import com.wet.eyetracking.utils.MyUtils
import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import org.opencv.objdetect.Objdetect
import java.io.File
import java.util.ArrayList

class EyeDetector(private val cascade: CascadeClassifier) {

    fun findEye(file: File): Point? {
        val rgba = Imgcodecs.imread(file.absolutePath, Imgcodecs.CV_LOAD_IMAGE_COLOR)
        val gray = Imgcodecs.imread(file.absolutePath, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE)

        val eyes = MatOfRect()

        cascade.detectMultiScale(gray, eyes)

        var iris: Point? = null
        val irises = ArrayList<Point>()

        val eyeAreas = eyes.toArray()
        for (i in eyeAreas.indices) {
            val eyeArea = eyeAreas[i]
            Imgproc.rectangle(rgba, eyeArea.tl(), eyeArea.br(),
                    Scalar(255.0, 0.0, 0.0, 255.0), 2)

            iris = getIris(gray, eyeArea)

            if (iris != null) {
                Imgproc.circle(rgba, Point(iris.x, iris.y), 2, Scalar(255.0, 255.0, 255.0, 255.0), 2)
                irises.add(iris)
                if (irises.size == 2) {
                    break
                }
            }

        }

        MyUtils.writeImage(file, rgba)

        return iris
    }

    fun getIris(mGray: Mat, area: Rect): Point? {
        var mROI = mGray.submat(area)
        val eyes = MatOfRect()
        val iris = Point()

        cascade.detectMultiScale(mROI, eyes, 1.15, 2,
                Objdetect.CASCADE_FIND_BIGGEST_OBJECT or Objdetect.CASCADE_SCALE_IMAGE, Size(30.0, 30.0),
                Size())

        val eyesArray = eyes.toArray()
        val i = 0
        while (i < eyesArray.size) {
            val e = eyesArray[i]
            e.x = area.x + e.x
            e.y = area.y + e.y
            val eye_only_rectangle = Rect(e.tl().x.toInt(),
                    (e.tl().y + e.height * 0.4).toInt(), e.width,
                    (e.height * 0.6).toInt())
            mROI = mGray.submat(eye_only_rectangle)

            val mmG = Core.minMaxLoc(mROI)

            iris.x = mmG.minLoc.x + eye_only_rectangle.x
            iris.y = mmG.minLoc.y + eye_only_rectangle.y

            return iris
        }
        return null
    }
}