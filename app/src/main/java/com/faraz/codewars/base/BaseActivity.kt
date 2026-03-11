package com.faraz.codewars.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.faraz.codewars.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseActivity : ComponentActivity() {

    abstract fun tryAgain()

    private var _toolbarTitle = mutableStateOf("")
    private var _isToolbarVisible = mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setComposeContent(content: @Composable () -> Unit) {
        setContent {
            val title by _toolbarTitle
            val isVisible by _isToolbarVisible

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.contentContainerColor))
            ) {
                if (isVisible) {
                    BaseToolbar(
                        title = title,
                        onBackClick = { onBackPressedDispatcher.onBackPressed() }
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    content()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun BaseToolbar(title: String, onBackClick: () -> Unit) {
        TopAppBar(
            title = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = title,
                        color = colorResource(id = R.color.primaryTextColor),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",
                        tint = colorResource(id = R.color.primaryTextColor)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(id = R.color.contentContainerColor)
            )
        )
    }

    fun setToolbarVisible(isVisible: Boolean) {
        _isToolbarVisible.value = isVisible
    }

    fun setToolbarTitle(title: String) {
        _toolbarTitle.value = title
    }
}

