package cga.exercise.game.Pages


import cga.exercise.components.gui.*
import cga.exercise.components.gui.TextComponents.TextMode
import cga.exercise.components.gui.constraints.*
import cga.exercise.game.Settings
import cga.exercise.game.StaticResources
import cga.exercise.game.Tester

class MainMenuPage(startButtonOnClick : ((Int,Int) -> Unit), autoTesterButtonOnClick : ((Int,Int) -> Unit)) : LayoutBox(Relative(1f), Relative(1f), Center(), Center()) {

    var settings : Settings = Settings.loadSettings()

    var testScript : Tester? = Tester.loadTester()

    private val parallelToggleButtonOnValueChanged = { b : Boolean -> settings.executeParallel = b; jobCountTextBox.disable(!b) }

    private val jobCountOnValueChanged = { s : String -> settings.jobCount = (s).toIntOrNull() ?: 1 }

    private val jobCountTextBox = LayoutBox(Relative(0.98f), Relative(0.035f), PixelLeft(32), PixelTop(10), children = listOf(
        Text("How many jobs will be used. (Default: number of System Cores [${Runtime.getRuntime().availableProcessors()}] )", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
        NumberBox(settings.jobCount.toString(), PixelWidth(213), PixelHeight(25), PixelRight(60), Center(), textMode = TextMode.Center, fontSize = 2.5f, cursorColor = StaticResources.highlightColor, OnValueChanged = jobCountOnValueChanged)
    ))

    private val sliderUpdateFrequencyOnValueChanged = { f : Float -> settings.updateFrequency = (f * 599f + 1).toInt(); updateFrequencyText.text = "${settings.updateFrequency} per sec" }

    private val updateFrequencyText = Text("${settings.updateFrequency} per sec", 2.5f, StaticResources.standardFont,30f,TextMode.Right, false, PixelRight(60 + 213 + 10), Center(), StaticResources.fontColor1)

    private val sliderObjectCountOnValueChanged = { f : Float -> updateObjectCount(0, (f * countMultiplier).toInt()) }
    private val textBoxObjectCountOnValueChanged = { s : String -> updateObjectCount(1, (s).toIntOrNull() ?: 0) }

    private val countMultiplier = 16500

    private val sliderText = NumberBox(settings.objectCount.toString(), PixelWidth(60),PixelHeight(25), PixelRight(60 + 213 + 10), Center(), Color.nothing, StaticResources.fontColor1, TextMode.Right,false, 0, 2.5f, cursorColor = StaticResources.highlightColor, OnValueChanged = textBoxObjectCountOnValueChanged)
    private val slider = Slider((settings.objectCount / countMultiplier.toFloat()).coerceIn(0f,1f), PixelWidth(213), PixelHeight(25), PixelRight(60), Center(), sliderObjectCountOnValueChanged)

    private val useSampleDataToggleButtonOnValueChanged = { b : Boolean -> settings.useSampleData = b }

    private val testResultOutputToggleButtonOnValueChanged = { b : Boolean -> settings.shouldTestResultOutput = b; testResultOutput.forEach { it.disable(!b) } }

    //private val texResultPathOnValueChanged = { s : String -> settings.testResultPath = s }

    val testResultOutput = listOf(
        LayoutBox(Relative(0.98f), Relative(0.035f), PixelLeft(32), PixelTop(10), children = listOf(
            Text("How many update cycles will be executed.", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
            NumberBox("1000", PixelWidth(213), PixelHeight(25), PixelRight(60), Center(), fontSize = 2.5f, cursorColor = StaticResources.highlightColor)
        )),
//        LayoutBox(Relative(0.98f), Relative(0.035f), PixelLeft(32), PixelTop(10), children = listOf(
//            Text("Sets the output path.", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
//            //TextBox(settings.testResultPath, PixelWidth(213), PixelHeight(25), PixelRight(60), Center(), textMode = TextMode.Left, fontSize = 2.5f, cursorColor = StaticResources.highlightColor, OnValueChanged = texResultPathOnValueChanged)
//        ))
    )

    init {
        children = listOf(
            Scrollbar(Relative(1f), Relative(1f), Center(), Center(), true, false, StaticResources.backGroundColor, innerElement =
                LayoutBox(Relative(1f), Relative(1.5f), Center(), PixelTop(0), children = listOf(
                    UIList(Relative(1f), Relative(0.75f), Center(), PixelTop(0), true, children = listOf(
                        Text("Settings:", 5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(30), PixelTop(30)),

                        Text("Execute Parallel:", 3.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(30), PixelTop(35)),
                        LayoutBox(Relative(0.98f), Relative(0.035f), PixelLeft(32), PixelTop(10), children = listOf(
                            Text("The collision detection and gravity system are executed parallel.", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
                            ToggleButton(settings.executeParallel, PixelWidth(42),PixelHeight(25), PixelRight(60), Center(), true, parallelToggleButtonOnValueChanged)
                        )),
                        jobCountTextBox,

                        Text("Test2:", 3.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(30), PixelTop(28)),
                        LayoutBox(Relative(0.98f), Relative(0.07f), PixelLeft(32), PixelTop(10), children = listOf(
                            Text("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, \nsed diam nonumy eirmod tempor invidunt ut labore et.", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
                            ToggleButton(false, PixelWidth(42),PixelHeight(25), PixelRight(60), Center(), true)
                        )),
                        LayoutBox(Relative(0.98f), Relative(0.07f), PixelLeft(32), PixelTop(10), children = listOf(
                            Text("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, \nsed diam nonumy eirmod tempor invidunt ut labore et.", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
                            ToggleButton(false, PixelWidth(42),PixelHeight(25), PixelRight(60), Center(), true)
                        )),
                        Text("Updates:", 3.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(30), PixelTop(28)),
                        LayoutBox(Relative(0.98f), Relative(0.07f), PixelLeft(32), PixelTop(10), children = listOf(
                            Text("This setting is used to limit how many updates per Second \ncan occur. (The minimum amount of updates per Frame is one)", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
                            updateFrequencyText,
                            Slider(((settings.updateFrequency -1f) / 599f),PixelWidth(213), PixelHeight(25), PixelRight(60), Center(), sliderUpdateFrequencyOnValueChanged)
                        )),
                        Text("Objects:", 3.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(30), PixelTop(28)),
                        LayoutBox(Relative(0.98f), Relative(0.07f), PixelLeft(32), PixelTop(10), children = listOf(
                            Text("If true, predetermined sample data will be used to spawn \nthe selected amount of Objects.", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
                            ToggleButton(settings.useSampleData, PixelWidth(42),PixelHeight(25), PixelRight(60), Center(), true, useSampleDataToggleButtonOnValueChanged)
                        )),
                        LayoutBox(Relative(0.98f), Relative(0.035f), PixelLeft(32), PixelTop(10), children = listOf(
                            Text("How many Objects will be generated.", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
                            sliderText,
                            slider
                        )),
                        Text("Test Result Output:", 3.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(30), PixelTop(28)),
                        LayoutBox(Relative(0.98f), Relative(0.035f), PixelLeft(32), PixelTop(10), children = listOf(
                            Text("Should tests be executed.", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
                            ToggleButton(settings.shouldTestResultOutput, PixelWidth(42),PixelHeight(25), PixelRight(60), Center(), true, testResultOutputToggleButtonOnValueChanged)
                        )),
                        testResultOutput[0],
//                        testResultOutput[1],
                    )),

                    Button("AutoTester", PixelWidth(160), PixelHeight(60), PixelRight(55 + 160 + 10), PixelBottom(40), onClick = autoTesterButtonOnClick),
                    Button("Start", PixelWidth(160), PixelHeight(60), PixelRight(55), PixelBottom(40), onClick = startButtonOnClick)
            )))
        )


        jobCountTextBox.disable(!settings.executeParallel)
        testResultOutput.forEach { it.disable(!settings.shouldTestResultOutput) }

    }

    private fun updateObjectCount(sender : Int, value : Int){

        if(sender == 0)
            sliderText.text = value.toString()

        if(sender == 1)
            slider.value = (value / countMultiplier.toFloat()).coerceIn(0f,1f)

        settings.objectCount = value
    }

}