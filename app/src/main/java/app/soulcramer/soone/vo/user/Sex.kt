package app.soulcramer.soone.vo.user

import `fun`.soone.R


enum class Sex {
    MALE {
        override fun stringRes(): Int = R.string.sex_male

        override fun toInt(): Int = 1
    },
    FEMALE {
        override fun stringRes(): Int = R.string.sex_female

        override fun toInt(): Int = 2
    },
    OTHER {
        override fun stringRes(): Int = R.string.sex_other

        override fun toInt(): Int = 3
    };

    abstract fun toInt(): Int

    abstract fun stringRes(): Int

    companion object {
        fun fromInt(value: Int): Sex = when (value) {
            MALE.toInt() -> MALE
            FEMALE.toInt() -> FEMALE
            OTHER.toInt() -> OTHER
            else -> OTHER
        }
    }
}
