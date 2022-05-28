package cga.exercise.game.Pages

import cga.exercise.components.gui.*
import cga.exercise.components.gui.TextComponents.TextMode
import cga.exercise.components.gui.constraints.*
import cga.exercise.components.texture.Texture2D
import cga.exercise.game.StaticResources

class MainMenuPage(startButtonOnClick : ((Int,Int) -> Unit)) : LayoutBox(Relative(1f), Relative(1f), Center(), Center()) {


    var executeParallel = true
    private val parallelToggleButtonOnValueChanged = { b : Boolean -> executeParallel = b }

    var updateFrequency = 60
    private val sliderUpdateFrequencyOnValueChanged = { f : Float -> updateFrequency = (f * 599f + 1).toInt(); updateFrequencyText.text = "$updateFrequency per sec" }

    private val updateFrequencyText = Text("$updateFrequency per sec", 2.5f, StaticResources.standardFont,30f,TextMode.Right, false, PixelRight(60 + 213 + 10), Center(), StaticResources.fontColor1)

    private val sliderObjectCountOnValueChanged = { f : Float -> updateObjectCount(0, (f * countMultiplier).toInt()) }
    private val textBoxObjectCountOnValueChanged = {
            s : String -> updateObjectCount(1, (s).toIntOrNull() ?: 0)
    }

    private val countMultiplier = 10000
    var objectCount = 0


    private val sliderText = NumberBox("0", PixelWidth(60),PixelHeight(25), PixelRight(60 + 213 + 10), Center(), Color.nothing, StaticResources.fontColor1, TextMode.Right,false, 0, 2.5f, cursorColor = StaticResources.highlightColor, OnValueChanged = textBoxObjectCountOnValueChanged)
    private val slider = Slider(0f,PixelWidth(213), PixelHeight(25), PixelRight(60), Center(), sliderObjectCountOnValueChanged)

    var useSampleData = true
    private val useSampleDataToggleButtonOnValueChanged = { b : Boolean -> useSampleData = b }


    var shouldTestResultOutput = true
    private val testResultOutputToggleButtonOnValueChanged = { b : Boolean -> shouldTestResultOutput = b; testResultOutput.forEach { it.disable(!b) } }

    val testResultOutput = listOf(
        LayoutBox(Relative(0.98f), Relative(0.035f), PixelLeft(32), PixelTop(10), children = listOf(
            Text("How many update cycles will be executed.", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
            NumberBox("1000", PixelWidth(213), PixelHeight(25), PixelRight(60), Center(), fontSize = 2.5f, cursorColor = StaticResources.highlightColor)
        )),
        LayoutBox(Relative(0.98f), Relative(0.035f), PixelLeft(32), PixelTop(10), children = listOf(
            Text("Sets the output path.", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
            TextBox("out/result.txt", PixelWidth(213), PixelHeight(25), PixelRight(60), Center(), textMode = TextMode.Left, fontSize = 2.5f, cursorColor = StaticResources.highlightColor)
        ))
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
                            ToggleButton(executeParallel, PixelWidth(42),PixelHeight(25), PixelRight(60), Center(), true, parallelToggleButtonOnValueChanged)
                        )),

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
                            Slider(((updateFrequency -1f) / 599f),PixelWidth(213), PixelHeight(25), PixelRight(60), Center(), sliderUpdateFrequencyOnValueChanged)
                        )),
                        Text("Objects:", 3.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(30), PixelTop(28)),
                        LayoutBox(Relative(0.98f), Relative(0.07f), PixelLeft(32), PixelTop(10), children = listOf(
                            Text("If true, predetermined sample data will be used to spawn \nthe selected amount of Objects.", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
                            ToggleButton(useSampleData, PixelWidth(42),PixelHeight(25), PixelRight(60), Center(), true, useSampleDataToggleButtonOnValueChanged)
                        )),
                        LayoutBox(Relative(0.98f), Relative(0.035f), PixelLeft(32), PixelTop(10), children = listOf(
                            Text("How many Objects will be generated.", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
                            sliderText,
                            slider
                        )),
                        Text("Test Result Output:", 3.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(30), PixelTop(28)),
                        LayoutBox(Relative(0.98f), Relative(0.035f), PixelLeft(32), PixelTop(10), children = listOf(
                            Text("Should tests be executed.", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
                            ToggleButton(shouldTestResultOutput, PixelWidth(42),PixelHeight(25), PixelRight(60), Center(), true, testResultOutputToggleButtonOnValueChanged)
                        )),
                        testResultOutput[0],
                        testResultOutput[1],
                    )),

                    Button("Start", PixelWidth(160), PixelHeight(60), PixelRight(55), PixelBottom(40), onClick = startButtonOnClick)
    //        Text("Paralles Verarbeiten:",4f, StaticResources.standardFont,30f, TextMode.Left,false, Center(), PixelBottom(360), color = Color(255f,255f,255f)),
    //        ToggleButton(false,PixelWidth(80), PixelHeight(40), Center(), PixelBottom(320), true),
    //        Text("Anzahl Himmelskörper:",4f, StaticResources.standardFont,30f, TextMode.Left,false, Center(), PixelBottom(220), color = Color(255f,255f,255f)),
    //        textBoxCount,
    //        Button("Start", PixelWidth(200), PixelHeight(80), Center(), PixelBottom(20), onClick = startButtonOnClick),
            )))
        )

    }

    private fun updateObjectCount(sender : Int, value : Int){

        if(sender == 0)
            sliderText.text = value.toString()

        if(sender == 1)
            slider.value = (value / countMultiplier.toFloat()).coerceIn(0f,1f)


        objectCount = value
    }








}