


import java.util.ArrayList;

import edu.purdue.cs59000.umltranslator.*;
import edu.purdue.cs59000.umltranslator.exceptions.UMLSDStructureException;
import edu.purdue.cs59000.umltranslator.message.*;
import edu.purdue.cs59000.umltranslator.umlcontainer.*;

public class undergradExample {
	public static void main(String args[]) throws UMLSDStructureException
	{
		UMLSequenceDiagram umlSD = new UMLSequenceDiagram();
		
		/*
		 * Setup classes whose lifelines last the entire sequence
		 */
		
		UMLActor customer = new UMLActor("customer1","Customer",umlSD);
		UMLLifeline customerLifeline = new UMLLifeline(customer,umlSD);
		
		UMLClass chef = new UMLClass("pizzaChef","Chef",umlSD);
		UMLLifeline chefLifeline = new UMLLifeline(chef,umlSD);
		
		UMLClass oven = new UMLClass("pizzaOven","Oven",umlSD);
		UMLLifeline ovenLifeline = new UMLLifeline(oven,umlSD);
		
		UMLClass PizzaClass = new UMLClass("Pizza",umlSD);
		UMLLifeline PizzaClassLifeline = new UMLLifeline(PizzaClass,umlSD);
		
		
		/* 
		 * Sequentially instantiate diagram objects
		 */
		
		UMLActivationBox customerDining = new UMLActivationBox(customerLifeline,umlSD);
		
		// customer requests a pizza from the chef using a pizza order
		UMLClass pizzaOrder = new UMLClass("PizzaOrder",umlSD);
		UMLCreateMessage createPizzaOrder = new UMLCreateMessage("pizzaOrder",pizzaOrder,null,customerDining,umlSD);
		UMLLifeline pizzaOrderLifeline = new UMLLifeline(pizzaOrder,umlSD);
		createPizzaOrder.setDestination(pizzaOrder); // destinations of create messages should be UMLClasses
		
		UMLActivationBox chefCooking = new UMLActivationBox(chefLifeline,umlSD);
		
		UMLMessageArgument pizzaOrderArg = new UMLMessageArgument("pizzaOrder","PizzaOrder","pizzaOrder");
		ArrayList<UMLMessageArgument> pizzaOrderArgs = new ArrayList<UMLMessageArgument>();
		pizzaOrderArgs.add(pizzaOrderArg);
		UMLSynchronousMessage requestPizza = new UMLSynchronousMessage("foodOrder","Pizza",pizzaOrderArgs,customerDining,chefCooking,umlSD);
		
		// chef begins preparing the pizza dough
		
		UMLActivationBox chefPreparingDough = new UMLActivationBox(chefCooking,umlSD);
		
		// pizza dough is created
		UMLClass pizzaDough = new UMLClass("Dough",umlSD);
		UMLCreateMessage createPizzaDough = new UMLCreateMessage("pizzaDough",pizzaDough,null,chefPreparingDough,umlSD);
		UMLLifeline pizzaDoughLifeline = new UMLLifeline(pizzaDough,umlSD);
		UMLActivationBox doughPreparing = new UMLActivationBox(pizzaDoughLifeline,umlSD);
		
		// knead the dough until it's ready
		UMLCondition doughReady = new UMLCondition("!doughReady",umlSD);
		UMLLoop doughKneadLoop = new UMLLoop(doughReady,umlSD); // automatically sets doughReady's parent container as doughKneadLoop
		
		// kneading the dough
		UMLMessageArgument kneadDoughArg = new UMLMessageArgument("pizzaDough","Dough","pizzaDough");
		ArrayList<UMLMessageArgument> kneadDoughArgs = new ArrayList<UMLMessageArgument>();
		kneadDoughArgs.add(kneadDoughArg);
		UMLSynchronousMessage kneadDough = new UMLSynchronousMessage("kneadDough","boolean",kneadDoughArgs,chefPreparingDough,doughPreparing,umlSD);
		kneadDough.setParentContainer(doughReady); // need to contain messages with containers if inside conditionals
		
		UMLReturnMessage returnDoughReady = new UMLReturnMessage("doughReady",doughPreparing,chefPreparingDough,umlSD);
		returnDoughReady.setParentContainer(doughReady); // need to contain messages with containers if inside conditionals
	
		// dough is ready, add toppings
		
		UMLActivationBox chefAddToppings = new UMLActivationBox(chefCooking,umlSD);
		
		
		UMLClass toppedDough = new UMLClass("ToppedDough",umlSD);
		UMLMessageArgument topping = new UMLMessageArgument("pizzaDough","Dough","pizzaDough");
		ArrayList<UMLMessageArgument> toppings = new ArrayList<UMLMessageArgument>();
		toppings.add(topping);
		UMLCreateMessage addToppings = new UMLCreateMessage("toppedDough",toppedDough,toppings,toppedDough,umlSD);
		UMLLifeline toppedDoughLifeline = new UMLLifeline(toppedDough,umlSD);
		
		// determine the cheese type
		UMLCondition crispyCheese = new UMLCondition("CrispyCheese",umlSD);
		UMLElse normalCheese = new UMLElse(umlSD); // in the diagram, this is specified as "NormalCheese", which should just be else
		UMLAlternatives cheeseSelection = new UMLAlternatives(crispyCheese, normalCheese,umlSD); // automatically sets condition and 
		                                                                                   // else parent's as cheeseSelection
		
		// prepare the oven to bake
		UMLActivationBox ovenBaking = new UMLActivationBox(ovenLifeline,umlSD);
		
		// set the oven to bake for 30 minutes to make crispy cheese
		UMLSynchronousMessage crispyCheeseMsg = new UMLSynchronousMessage("cook","Pizza",null,chefCooking,ovenBaking,umlSD);
		crispyCheeseMsg.addArgument(new UMLMessageArgument("toppedDough","ToppedDough","toppedDough"));
		crispyCheeseMsg.addArgument(new UMLMessageArgument("cookTime","int","30"));
		crispyCheeseMsg.setParentContainer(crispyCheese); // need to contain messages with containers if inside conditionals
		
		// set the oven to bake for 20 minutes to make normal cheese
		UMLSynchronousMessage normalCheeseMsg = new UMLSynchronousMessage("cook","Pizza",null,chefCooking,ovenBaking,umlSD);
		normalCheeseMsg.addArgument(new UMLMessageArgument("toppedDough","ToppedDough","toppedDough"));
		normalCheeseMsg.addArgument(new UMLMessageArgument("cookTime","int","20"));
		normalCheeseMsg.setParentContainer(normalCheese); // need to contain messages with containers if inside conditionals
		
		// prepare to create a pizza
		UMLActivationBox createPizzaActivation = new UMLActivationBox(PizzaClassLifeline,umlSD);
		
		// start cooking the pizza
		UMLSynchronousMessage createPizza = new UMLSynchronousMessage("createPizza","Pizza",null,ovenBaking,createPizzaActivation,umlSD);
		createPizza.addArgument(new UMLMessageArgument("toppedDough","ToppedDough","toppedDough"));
		createPizza.addArgument(new UMLMessageArgument("cookTime","int","cookTime"));
		
		// use the Pizza class to create a pizza
		UMLClass cookedPizza = new UMLClass("Pizza",umlSD);
		UMLCreateMessage createCookedPizza = new UMLCreateMessage("cookedPizza",cookedPizza,null,createPizzaActivation,umlSD);
		
		UMLLifeline cookedPizzaLifeline = new UMLLifeline(cookedPizza,umlSD);
		
		// oven completes baking the pizza 
		UMLReturnMessage returnCookedPizzaToOven = new UMLReturnMessage("cookedPizza",createPizzaActivation,ovenBaking,umlSD);
		
		// chef removes the pizza from the oven
		UMLReturnMessage returnCookedPizzaToChef = new UMLReturnMessage("cookedPizza",ovenBaking,chefCooking,umlSD);
		
		// chef sets the pizza to cool; use the pizza class to determine when cooling is complete
		UMLActivationBox coolPizzaActivation = new UMLActivationBox(PizzaClassLifeline,umlSD);
		
		// start cooling the pizza
		UMLSynchronousMessage coolPizza = new UMLSynchronousMessage("cool","Pizza",null,chefCooking,coolPizzaActivation,umlSD);
		coolPizza.addArgument(new UMLMessageArgument("cookedPizza","Pizza","cookedPizza"));
		
		// cool the pizza until it's ready
		UMLCondition cooled = new UMLCondition("!cooled",umlSD);
		UMLLoop pizzaCoolingLoop = new UMLLoop(cooled,umlSD);
		
		// cooling cycle
		UMLSynchronousMessage coolCycle = new UMLSynchronousMessage("coolCycle","boolean",null,coolPizzaActivation,coolPizzaActivation,umlSD);
		coolCycle.addArgument(new UMLMessageArgument("cookedPizza","Pizza","cookedPizza"));
		coolCycle.setParentContainer(cooled); // need to contain messages with containers if inside conditionals
		
		// return cooling status
		UMLReturnMessage coolPizzaStatus = new UMLReturnMessage("cooled",coolPizzaActivation,coolPizzaActivation,umlSD);
		coolPizzaStatus.setParentContainer(cooled); // need to contain messages with containers if inside conditionals
		
		// if pizza is cooled, then return finished pizza to chef
		UMLReturnMessage pizzaCooled = new UMLReturnMessage("finishedPizza",coolPizzaActivation,chefCooking,umlSD);
		
		// deliver the finished pizza to the customer
		UMLReturnMessage deliverPizza = new UMLReturnMessage("finishedPizza",chefCooking,customerDining,umlSD);
	}
}

