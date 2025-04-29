package com.mine.flowpay

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mine.flowpay.model.Mail
import com.mine.flowpay.network.RetrofitInstance
import com.mine.flowpay.utils.DialogUtils
import kotlinx.coroutines.launch

class MailDetailsActivity : AppCompatActivity() {
    private lateinit var backButton: ImageView
    private lateinit var deleteButton: ImageView
    private lateinit var subjectTextView: TextView
    private lateinit var messageTextView: TextView
    private lateinit var currentMail: Mail
    private var mailId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_details)

        // Get mail ID from intent
        mailId = intent.getLongExtra("MAIL_ID", -1)
        if (mailId == -1L) {
            finish()
            return
        }

        // Set header title
        val headerTitle = findViewById<TextView>(R.id.tv_title)
        headerTitle.text = "Mail Details"

        // Initialize views
        backButton = findViewById(R.id.iv_back)
        deleteButton = findViewById(R.id.iv_delete)
        subjectTextView = findViewById(R.id.tv_subject)
        messageTextView = findViewById(R.id.tv_message)

        // Load mail details from backend
        loadMailDetails()

        // Set up back button
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Set up delete button
        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun loadMailDetails() {
        lifecycleScope.launch {
            val response = RetrofitInstance.api.getMailById(mailId)
            if (response.isSuccessful) {
                val mail = response.body()
                if (mail != null) {
                    currentMail = mail
                    // Mark mail as read if not already
                    if (!mail.isRead) {
                        RetrofitInstance.api.updateMail(mail.copy(isRead = true))
                    }
                    setResult(RESULT_OK)
                    subjectTextView.text = mail.subject
                    messageTextView.text = mail.message
                    // If this mail is associated with a transaction, show transaction button
                    if (mail.transaction_id != null) {
                        val transactionButton = findViewById<TextView>(R.id.btn_view_transaction)
                        transactionButton?.apply {
                            text = "Purchase Receipt"
                            setTextColor(resources.getColor(R.color.blue, null))
                            setOnClickListener {
                                val intent = Intent(
                                    this@MailDetailsActivity,
                                    TransactionDetails::class.java
                                ).apply {
                                    putExtra("TRANSACTION_ID", mail.transaction_id)
                                }
                                startActivity(intent)
                            }
                            visibility = android.view.View.VISIBLE
                        }
                    }
                } else {
                    finish()
                }
            } else {
                finish()
            }
        }
    }

    private fun showDeleteConfirmationDialog() {
        DialogUtils.showCustomConfirmationDialog(
            context = this,
            title = "Delete Mail",
            message = "Are you sure you want to delete this mail?",
            onConfirm = {
                deleteMail()
            }
        )
    }

    private fun deleteMail() {
        lifecycleScope.launch {
            val response = RetrofitInstance.api.deleteMail(currentMail.mail_id)
            if (response.isSuccessful) {
                Toast.makeText(this@MailDetailsActivity, "Mail deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MailDetailsActivity, "Failed to delete mail", Toast.LENGTH_SHORT).show()
            }
            setResult(RESULT_OK)
            finish()
        }
    }
}
