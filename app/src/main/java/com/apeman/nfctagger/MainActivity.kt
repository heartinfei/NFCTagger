package com.apeman.nfctagger

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcF
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val techListsArray = arrayOf(arrayOf(NfcF::class.java.name))
    private val adapter: NfcAdapter by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }

    private val pendingIntent: PendingIntent by lazy {
        Intent(this, javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }.let {
            PendingIntent.getActivity(
                this,
                0,
                it,
                0
            )
        }
    }

    private val intentFiltersArray: Array<IntentFilter> by lazy {
        IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply {
            try {
                /* Handles all MIME based dispatches.
                 * You should specify only the ones that you need. */
                addDataType("*/*")
            } catch (e: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException("fail", e)
            }
        }.let {
            arrayOf(it)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    public override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val tagFromIntent: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        //do something with tagFromIntent

    }

    override fun onResume() {
        super.onResume()
        adapter.enableForegroundDispatch(this,
            pendingIntent,
            intentFiltersArray,
            techListsArray)
    }

    override fun onPause() {
        super.onPause()
        adapter.disableForegroundDispatch(this)
    }
}

