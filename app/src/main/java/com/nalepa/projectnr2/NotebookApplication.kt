package com.nalepa.projectnr2

import android.app.Application
import com.nalepa.projectnr2.room.NoteRoomDatabase

class NotebookApplication: Application() {
    val database: NoteRoomDatabase by lazy {NoteRoomDatabase.getMyDatabase(this)}
}