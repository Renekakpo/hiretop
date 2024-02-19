package com.example.hiretop.ui.extras

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hiretop.R

@Composable
fun DropdownListWords(
    modifier: Modifier = Modifier,
    title: String,
    items: Array<String>,
    onItemSelected: (String) -> Unit
) {
    val mWidth = LocalConfiguration.current.screenWidthDp.dp
    var selectedIndex by remember { mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }

    Column {
        if (title.isNotEmpty()) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }

        Row(
            modifier = modifier
                .height(height = 60.dp)
                .border(
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    shape = MaterialTheme.shapes.small
                )
                .clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = items[selectedIndex],
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = stringResource(R.string.down_arrow_icon_desc_text),
                modifier = Modifier
                    .size(32.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = modifier
                .width(IntrinsicSize.Max)
                .border(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small
                )
        ) {
            for (index in items.indices) {
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        selectedIndex = index
                        onItemSelected(items[index])
                    },
                    text = {
                        Row {
                            if (index == selectedIndex) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = stringResource(R.string.checked_icon_desc_text),
                                    modifier = Modifier.size(24.dp)
                                )

                                Spacer(modifier = Modifier.width(5.dp))
                            }

                            Text(
                                text = items[index],
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp),
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = index == selectedIndex,
                            onClick = { expanded = false }
                        )
                )
            }
        }
    }
}