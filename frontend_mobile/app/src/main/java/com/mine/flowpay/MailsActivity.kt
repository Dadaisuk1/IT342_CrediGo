package com.mine.flowpay

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mine.flowpay.adapters.MailAdapter
import com.mine.flowpay.app.FlowpayApp
import com.mine.flowpay.fragments.NavbarFragment
import com.mine.flowpay.utils.DialogUtils
import com.mine.flowpay.viewmodel.MailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MailsActivity : AppCompatActivity() {
    private var mailObserver: androidx.lifecycle.Observer<Int>? = null
    companion object {
        private const val MAIL_DETAIL_REQUEST_CODE = 100
    }
    private lateinit var mailViewModel: MailViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: ImageView

    private var currentUserId: Long = -1

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MAIL_DETAIL_REQUEST_CODE && resultCode == RESULT_OK) {
            // Refresh mail list
            loadUserMailsFromBackend(currentUserId)
            // Set result to update parent activity
            setResult(RESULT_OK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mails)

        // Set up navbar
        supportFragmentManager.beginTransaction()
            .replace(R.id.navbar_container, NavbarFragment())
            .commit()

        // Initialize ViewModels
        mailViewModel = ViewModelProvider(this).get(MailViewModel::class.java)

        // Get user ID from FlowpayApp
        val app = application as FlowpayApp
        currentUserId = app.loggedInuser?.user_id ?: -1

        // Initialize views
        recyclerView = findViewById(R.id.rv_mails)
        backButton = findViewById(R.id.iv_back)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Get mails for the current user
        loadUserMailsFromBackend(currentUserId)

        // Observe mails
        mailViewModel.userMails.observe(this, Observer { mails ->
            recyclerView.adapter = MailAdapter(
                mails = mails,
                onMailClicked = { mail ->
                    // Handle mail click - navigate to MailDetailsActivity
                    val intent = Intent(this, MailDetailsActivity::class.java).apply {
                        putExtra("MAIL_ID", mail.mail_id)
                    }
                    startActivityForResult(intent, MAIL_DETAIL_REQUEST_CODE)
                },
                onMailLongClicked = { mail ->
                    // Handle long press - show delete confirmation dialog
                    DialogUtils.showCustomConfirmationDialog(
                        context = this,
                        title = "Delete Mail",
                        message = "Are you sure you want to delete this mail?",
                        onConfirm = {
                            // Delete mail
                            CoroutineScope(Dispatchers.IO).launch {
                                mailViewModel.deleteMail(mail)

                                // Refresh mail list
                                runOnUiThread {
                                    loadUserMailsFromBackend(currentUserId)
                                    Toast.makeText(this@MailsActivity, "Mail deleted", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                    true
                }
            )
        })

        // Set header title and back button
        val headerTitle = findViewById<TextView>(R.id.tv_header_title)
        headerTitle.text = "Mails"
        findViewById<ImageView>(R.id.iv_back).setOnClickListener { onBackPressed() }

        // Set up mail observer
        mailObserver = androidx.lifecycle.Observer<Int> { count ->
            setResult(RESULT_OK) // Ensure parent activity updates its mail icon
        }

        // Initial mail check
        mailViewModel.unreadMailCount.observe(this, mailObserver!!)
    }

    private fun loadUserMailsFromBackend(userId: Long) {
        // This function is for backend DB connectivity only, no UI/logic changes
        // Example usage: mailViewModel.getUserMails(userId)
        mailViewModel.getUserMails(userId)
    }
}
