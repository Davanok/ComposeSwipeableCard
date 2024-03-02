package //TODO

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun SwipeableCardUsage() {
    val swipeAbleCardState = rememberSwipeableCardState()

    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(100) {
            SwipeableCard(
                state = swipeAbleCardState,
                key = { it },
                startContent = {
                    FilledIconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "")
                    }
                },
                endContent = {
                    FilledIconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "")
                    }
                    FilledIconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                    }
                }
            ) {
                Card (
                    modifier = Modifier.fillMaxWidth().height(80.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "item: $it"
                    )
                }
            }
        }
    }
}
