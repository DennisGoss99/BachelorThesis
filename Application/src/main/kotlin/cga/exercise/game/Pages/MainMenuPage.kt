package cga.exercise.game.Pages


import cga.exercise.components.gui.*
import cga.exercise.components.gui.TextComponents.TextMode
import cga.exercise.components.gui.constraints.*
import cga.exercise.components.texture.Texture2D
import cga.exercise.game.Settings
import cga.exercise.game.StaticResources
import cga.exercise.game.Tester
import kotlin.random.Random

class MainMenuPage(startButtonOnClick : ((Int,Int) -> Unit)) : LayoutBox(Relative(1f), Relative(1f), Center(), Center()) {

    var settings : Settings = Settings.loadSettings()

    private val parallelToggleButtonOnValueChanged = { b : Boolean -> settings.executeParallel = b; jobCountTextBox.disable(!b) }

    private val renderObjectsToggleButtonOnValueChanged = { b : Boolean -> settings.renderObjects = b}
    private val renderVisualsToggleButtonOnValueChanged = { b : Boolean -> settings.renderVisuals = b}
    private val evaluateCollisionsToggleButtonOnValueChanged = { b : Boolean -> settings.evaluateCollisions = b}
    private val applyCollisionEffectToggleButtonOnValueChanged = { b : Boolean -> settings.applyCollisionEffect = b}
    private val applyGravityToggleButtonOnValueChanged = { b : Boolean -> settings.applyGravityEffect = b}

    private val jobCountOnValueChanged = { s : String -> settings.jobCount = (s).toIntOrNull() ?: 1 }

    private val jobCountTextBox = LayoutBox(Relative(0.98f), Relative(0.035f), PixelLeft(32), PixelTop(10), children = listOf(
        Text("How many jobs will be used. (Default: number of System Cores [${Runtime.getRuntime().availableProcessors()}] )", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
        NumberBox(settings.jobCount.toString(), PixelWidth(213), PixelHeight(25), PixelRight(60), Center(), textMode = TextMode.Center, fontSize = 2.5f, cursorColor = StaticResources.highlightColor, OnValueChanged = jobCountOnValueChanged)
    ))

    private val sliderUpdateFrequencyOnValueChanged = { f : Float -> settings.updateFrequency = (f * 599f + 1).toInt(); updateFrequencyText.text = "${settings.updateFrequency} per sec" }

    private val updateFrequencyText = Text("${settings.updateFrequency} per sec", 2.5f, StaticResources.standardFont,30f,TextMode.Right, false, PixelRight(60 + 213 + 10), Center(), StaticResources.fontColor1)

    private val sliderObjectCountOnValueChanged = { f : Float ->
        updateObjectCount(0, (f * countMultiplier).toInt())
    }
    private val textBoxObjectCountOnValueChanged = { s : String ->
        updateObjectCount(1, (s).toIntOrNull() ?: 0)
    }

    private val countMultiplier = 16500

    private val sliderText = NumberBox(settings.objectCount.toString(), PixelWidth(60),PixelHeight(25), PixelRight(60 + 213 + 10), Center(), Color.nothing, StaticResources.fontColor1, TextMode.Right,false, 0, 2.5f, cursorColor = StaticResources.highlightColor, OnValueChanged = textBoxObjectCountOnValueChanged)
    private val slider = Slider((settings.objectCount / countMultiplier.toFloat()).coerceIn(0f,1f), PixelWidth(213), PixelHeight(25), PixelRight(60), Center(), sliderObjectCountOnValueChanged)

    private val sampleCount = 16500
    private val useSampleDataToggleButtonOnValueChanged = { b : Boolean -> settings.useSampleData = b; if (b) setUseSampleData() }



    private val refreshIcon = Texture2D("assets/textures/gui/refreshIcon.png", true)
    private val refreshButton = Image(refreshIcon, PixelWidth(25),PixelHeight(25), PixelRight(60), Center(), StaticResources.backGroundColor)

    private val seedNumberBox = NumberBox(settings.seed.toString(), Relative(0.16f), PixelHeight(25), PixelRight(90), Center(), fontSize = 2.5f, textMode = TextMode.Right, OnValueChanged = {s -> settings.seed = if (s.isBlank()) 0 else s.toLong() })

    private val onRefreshButtonClick : ((Int, Int) -> Unit) = {_, _ -> settings.seed = Random.nextLong(); seedNumberBox.text = settings.seed.toString(); }

    init {
        refreshButton.onClick = onRefreshButtonClick
    }

    private val shatterAmountText = Text(settings.shatterAmount.toString(), 2.5f, StaticResources.standardFont,30f,TextMode.Right, false, PixelRight(60 + 213 + 10), Center(), StaticResources.fontColor1)
    private val sliderShatterAmountOnValueChanged = { f : Float -> settings.shatterAmount = (f * 30f).toInt(); shatterAmountText.text = settings.shatterAmount.toString() }

    private val impactVelocityText = Text("%.2f".format(settings.impactVelocity), 2.5f, StaticResources.standardFont,30f,TextMode.Right, false, PixelRight(60 + 213 + 10), Center(), StaticResources.fontColor1)
    private val sliderImpactVelocityOnValueChanged = { f : Float -> settings.impactVelocity = (f * 20f); impactVelocityText.text = "%.2f".format(settings.impactVelocity)}


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

                        Text("Effects:", 3.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(30), PixelTop(28)),
                            LayoutBox(Relative(0.98f), Relative(0.035f), PixelLeft(32), PixelTop(10), children = listOf(
                                Text("Render anything to the Screen", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
                                ToggleButton(settings.renderObjects, PixelWidth(42),PixelHeight(25), PixelRight(60), Center(), true, renderObjectsToggleButtonOnValueChanged)
                            )),
                            LayoutBox(Relative(0.98f), Relative(0.035f), PixelLeft(32), PixelTop(10), children = listOf(
                                Text("Render visual effects", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
                                ToggleButton(settings.renderVisuals, PixelWidth(42),PixelHeight(25), PixelRight(60), Center(), true, renderVisualsToggleButtonOnValueChanged)
                            )),
                            LayoutBox(Relative(0.98f), Relative(0.035f), PixelLeft(32), PixelTop(10), children = listOf(
                                Text("Check if boxes collied", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
                                ToggleButton(settings.evaluateCollisions, PixelWidth(42),PixelHeight(25), PixelRight(60), Center(), true, evaluateCollisionsToggleButtonOnValueChanged)
                            )),
                            LayoutBox(Relative(0.98f), Relative(0.035f), PixelLeft(32), PixelTop(10), children = listOf(
                                Text("Apply shatter/ bounce off effect after collision", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
                                ToggleButton(settings.applyCollisionEffect, PixelWidth(42),PixelHeight(25), PixelRight(60), Center(), true, applyCollisionEffectToggleButtonOnValueChanged)
                            )),
                            LayoutBox(Relative(0.98f), Relative(0.035f), PixelLeft(32), PixelTop(10), children = listOf(
                                Text("Apply gravity effect to boxes", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
                                ToggleButton(settings.applyGravityEffect, PixelWidth(42),PixelHeight(25), PixelRight(60), Center(), true, applyGravityToggleButtonOnValueChanged)
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
                        Text("Shatter:", 3.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(30), PixelTop(28)),
                            LayoutBox(Relative(0.98f), Relative(0.035f), PixelLeft(32), PixelTop(10), children = listOf(
                                Text("The seed value determines output of generated random values.", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
                                seedNumberBox,
                                refreshButton,
                            )),
                            LayoutBox(Relative(0.98f), Relative(0.07f), PixelLeft(32), PixelTop(10), children = listOf(
                                Text("If two objects collied and shatter, then this value will determine the \nfragment count.", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
                                shatterAmountText,
                                Slider((settings.shatterAmount / 30f),PixelWidth(213), PixelHeight(25), PixelRight(60), Center(), sliderShatterAmountOnValueChanged)
                            )),
                            LayoutBox(Relative(0.98f), Relative(0.07f), PixelLeft(32), PixelTop(10), children = listOf(
                                Text("If the collision critical impact velocity is greater than the set Value \nthe collided objects will shatter.", 2.5f, StaticResources.standardFont,30f,TextMode.Left, true, PixelLeft(0), Center(), StaticResources.fontColor1),
                                impactVelocityText,
                                Slider((settings.impactVelocity / 20f), PixelWidth(213), PixelHeight(25), PixelRight(60), Center(), sliderImpactVelocityOnValueChanged)
                            )),
                    )),

                    Button("Start", PixelWidth(160), PixelHeight(60), PixelRight(55), PixelBottom(40), onClick = startButtonOnClick)
            )))
        )

        jobCountTextBox.disable(!settings.executeParallel)

    }

    private fun updateObjectCount(sender : Int, value : Int){

        if (settings.useSampleData && value > sampleCount){
            sliderText.text = sampleCount.toString()
            slider.value = (sampleCount / countMultiplier.toFloat()).coerceIn(0f,1f)
            settings.objectCount = sampleCount
            return
        }

        if(sender == 0)
            sliderText.text = value.toString()

        if(sender == 1)
            slider.value = (value / countMultiplier.toFloat()).coerceIn(0f,1f)

        settings.objectCount = value
    }

    private fun setUseSampleData(){
        if(settings.objectCount > sampleCount){
            sliderText.text = sampleCount.toString()
            slider.value = (sampleCount / countMultiplier.toFloat()).coerceIn(0f,1f)
            settings.objectCount = sampleCount
        }
    }

}