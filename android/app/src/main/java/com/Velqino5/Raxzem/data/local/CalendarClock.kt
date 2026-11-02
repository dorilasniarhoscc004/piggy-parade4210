package com.Velqino5.Raxzem.data.local

import java.time.LocalDate

object CalendarClock {

    fun today(): String = LocalDate.now().toString()

    fun yesterday(): String = LocalDate.now().minusDays(1).toString()
}
