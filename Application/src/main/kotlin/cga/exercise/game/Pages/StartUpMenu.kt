package cga.exercise.game.Pages

import cga.exercise.components.gui.Button
import cga.exercise.components.gui.LayoutBox
import cga.exercise.components.gui.constraints.*
import cga.exercise.game.Tester

class StartUpMenu(manualButtonOnClick : ((Int,Int) -> Unit), autoTesterButtonOnClick : ((Int,Int) -> Unit), exitButtonOnClick : ((Int,Int) -> Unit),)  : LayoutBox(Relative(1f), Relative(1f), Center(), Center()){

    var testScript : Tester? = Tester.loadTester()


    init {
        children = listOf(
            Button("ManualMode", PixelWidth(200), PixelHeight(60), PixelLeft(50), PixelBottom(50 + 65*2), onClick = manualButtonOnClick),
            Button("AutoTester", PixelWidth(200), PixelHeight(60), PixelLeft(50), PixelBottom(50 + 65), onClick = autoTesterButtonOnClick),
            Button("Exit", PixelWidth(200), PixelHeight(60), PixelLeft(50), PixelBottom(50), onClick = exitButtonOnClick),
        )
    }


}