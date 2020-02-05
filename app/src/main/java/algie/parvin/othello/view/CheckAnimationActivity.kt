package algie.parvin.othello.view

import algie.parvin.othello.R
import android.graphics.drawable.AnimatedVectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import kotlinx.android.synthetic.main.activity_check_animation.*

class CheckAnimationActivity : AppCompatActivity() {

    var white = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_animation)

        imageView.setImageResource(R.drawable.white_to_black_avd)
        imageView2.setImageResource(R.drawable.white_to_black_avd)

        button.setOnClickListener {
            if (white) {
                imageView.setImageResource(R.drawable.white_to_black_avd)
            } else {
                imageView.setImageResource(R.drawable.black_to_white_avd)
            }
            white = !white

            val drawable = imageView.drawable
            if (drawable is AnimatedVectorDrawable) {
                drawable.start()
            }
            if (drawable is AnimatedVectorDrawableCompat) {
                drawable.start()
            }
        }
    }
}
