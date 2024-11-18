package com.example.tiendadevinilos.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.tiendadevinilos.R


@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    minimizedMaxLines: Int = 2
) {
    val (isExpanded, setIsExpanded) = remember { mutableStateOf(false) }
    val (hasOverflow, setHasOverflow) = remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = text,
            color = colorResource(R.color.product_price),
            maxLines = if (isExpanded) Int.MAX_VALUE else minimizedMaxLines,
            onTextLayout = { textLayoutResult ->
                setHasOverflow(textLayoutResult.hasVisualOverflow)
            },
            modifier = Modifier.animateContentSize()
        )

        if (hasOverflow || isExpanded) {
            Text(
                text = if (isExpanded) "Ver menos" else "Ver más",
                style = MaterialTheme.typography.bodySmall,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier
                    .clickable { setIsExpanded(!isExpanded) }
                    .padding(vertical = 4.dp)
            )
        }
    }
}