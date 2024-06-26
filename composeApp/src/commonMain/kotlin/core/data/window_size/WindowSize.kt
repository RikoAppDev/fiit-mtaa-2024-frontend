package core.data.window_size

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

data class ScreenSizeInfo(val hPX: Int, val wPX: Int, val hDP: Dp, val wDP: Dp)
@Composable
expect fun getScreenSizeInfo(): ScreenSizeInfo