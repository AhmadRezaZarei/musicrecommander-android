package code.name.monkey.retromusic.activities.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import code.name.monkey.retromusic.R
import com.google.android.material.chip.Chip

class GenderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gender)


        val maleChip = findViewById<Chip>(R.id.chip_male).apply {
            isCheckable = true
        }
        val femaleChip = findViewById<Chip>(R.id.chip_female).apply {
            isCheckable = true
        }
        val ratherNotSayChip = findViewById<Chip>(R.id.chip_rather_not_say).apply {
            isCheckable = true
        }


        maleChip.setOnCheckedChangeListener { compoundButton, b ->

            if (b) {
                femaleChip.isChecked = false
                ratherNotSayChip.isChecked = false
            }

        }


        femaleChip.setOnCheckedChangeListener { compoundButton, b ->

            if (b) {
                maleChip.isChecked = false
                ratherNotSayChip.isChecked = false
            }

        }


        ratherNotSayChip.setOnCheckedChangeListener { compoundButton, b ->

            if (b) {
                femaleChip.isChecked = false
                maleChip.isChecked = false
            }

        }

    }
}