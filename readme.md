A library to parse and simplify Algebraic Expressions.

# Parsing a expression

Use AlgebraicExpression.parse(String). It throws a parsing exception and a runtime exception. You can use the normal operations symbols ("+","-","*","/","^") but no other operations are fully supported yet. To add custom variables or operations, a ComponentReference object must be created and initialized with the corresponding new objects. The ComponentReference must then be passed through the parsing method. This is the recommended way of parsing a expression since it becomes relevent when the evaluate method is used.

# Creating custom expression

You can also use the algebraic expression constructor with a variable to create a expression and use the append function to add a custom operation or custom variable (extending respectively Op_Operation and Var_Base).

# Simplifying

Use the simplify function to simplify the expression. This will not replace variables by their values defined in the ComponentReference object.

## Functions

The functions object are a little weird, you call them while parsing by simply writing
\<name of function\>(\<value of x in the function\>) while parsing but you need to define the value of the function called this way in the AlgebraicVarValues object by using the functions List object. To use it retrieve the ComponentReference object used while parsing and get the AlgebraicVarValues object inside of it. It is recommended to call your own ComponentReference object before parsing.

# Evaluate

Use either the evaluate(String name, Var_Base newValue) method or the evaluate(AlgebraicVarValuse values) object. The first method replaces each variables one at a time, the second replaces each values entered in the AlgebraicVarValues object. To use this get the ComponentReference object used while parsing and use the AlgebraicVarValues object inside of it or create a new AlgebraicVarValues Object.

