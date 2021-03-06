/**
 */
package org.archstudio.prolog.xtext.prolog;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.archstudio.prolog.xtext.prolog.Expression#getExps <em>Exps</em>}</li>
 *   <li>{@link org.archstudio.prolog.xtext.prolog.Expression#getOps <em>Ops</em>}</li>
 *   <li>{@link org.archstudio.prolog.xtext.prolog.Expression#getAtom <em>Atom</em>}</li>
 *   <li>{@link org.archstudio.prolog.xtext.prolog.Expression#isPrefix <em>Prefix</em>}</li>
 *   <li>{@link org.archstudio.prolog.xtext.prolog.Expression#getTerms <em>Terms</em>}</li>
 *   <li>{@link org.archstudio.prolog.xtext.prolog.Expression#getVariable <em>Variable</em>}</li>
 *   <li>{@link org.archstudio.prolog.xtext.prolog.Expression#getString <em>String</em>}</li>
 *   <li>{@link org.archstudio.prolog.xtext.prolog.Expression#getNumber <em>Number</em>}</li>
 *   <li>{@link org.archstudio.prolog.xtext.prolog.Expression#isList <em>List</em>}</li>
 *   <li>{@link org.archstudio.prolog.xtext.prolog.Expression#getHead <em>Head</em>}</li>
 *   <li>{@link org.archstudio.prolog.xtext.prolog.Expression#getTail <em>Tail</em>}</li>
 *   <li>{@link org.archstudio.prolog.xtext.prolog.Expression#isParen <em>Paren</em>}</li>
 *   <li>{@link org.archstudio.prolog.xtext.prolog.Expression#getSub <em>Sub</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.archstudio.prolog.xtext.prolog.PrologPackage#getExpression()
 * @model
 * @generated
 */
public interface Expression extends EObject
{
  /**
   * Returns the value of the '<em><b>Exps</b></em>' containment reference list.
   * The list contents are of type {@link org.archstudio.prolog.xtext.prolog.Expression}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Exps</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Exps</em>' containment reference list.
   * @see org.archstudio.prolog.xtext.prolog.PrologPackage#getExpression_Exps()
   * @model containment="true"
   * @generated
   */
  EList<Expression> getExps();

  /**
   * Returns the value of the '<em><b>Ops</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Ops</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Ops</em>' attribute list.
   * @see org.archstudio.prolog.xtext.prolog.PrologPackage#getExpression_Ops()
   * @model unique="false"
   * @generated
   */
  EList<String> getOps();

  /**
   * Returns the value of the '<em><b>Atom</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Atom</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Atom</em>' attribute.
   * @see #setAtom(String)
   * @see org.archstudio.prolog.xtext.prolog.PrologPackage#getExpression_Atom()
   * @model
   * @generated
   */
  String getAtom();

  /**
   * Sets the value of the '{@link org.archstudio.prolog.xtext.prolog.Expression#getAtom <em>Atom</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Atom</em>' attribute.
   * @see #getAtom()
   * @generated
   */
  void setAtom(String value);

  /**
   * Returns the value of the '<em><b>Prefix</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Prefix</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Prefix</em>' attribute.
   * @see #setPrefix(boolean)
   * @see org.archstudio.prolog.xtext.prolog.PrologPackage#getExpression_Prefix()
   * @model
   * @generated
   */
  boolean isPrefix();

  /**
   * Sets the value of the '{@link org.archstudio.prolog.xtext.prolog.Expression#isPrefix <em>Prefix</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Prefix</em>' attribute.
   * @see #isPrefix()
   * @generated
   */
  void setPrefix(boolean value);

  /**
   * Returns the value of the '<em><b>Terms</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Terms</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Terms</em>' containment reference.
   * @see #setTerms(Expression)
   * @see org.archstudio.prolog.xtext.prolog.PrologPackage#getExpression_Terms()
   * @model containment="true"
   * @generated
   */
  Expression getTerms();

  /**
   * Sets the value of the '{@link org.archstudio.prolog.xtext.prolog.Expression#getTerms <em>Terms</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Terms</em>' containment reference.
   * @see #getTerms()
   * @generated
   */
  void setTerms(Expression value);

  /**
   * Returns the value of the '<em><b>Variable</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Variable</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Variable</em>' attribute.
   * @see #setVariable(String)
   * @see org.archstudio.prolog.xtext.prolog.PrologPackage#getExpression_Variable()
   * @model
   * @generated
   */
  String getVariable();

  /**
   * Sets the value of the '{@link org.archstudio.prolog.xtext.prolog.Expression#getVariable <em>Variable</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Variable</em>' attribute.
   * @see #getVariable()
   * @generated
   */
  void setVariable(String value);

  /**
   * Returns the value of the '<em><b>String</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>String</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>String</em>' attribute.
   * @see #setString(String)
   * @see org.archstudio.prolog.xtext.prolog.PrologPackage#getExpression_String()
   * @model
   * @generated
   */
  String getString();

  /**
   * Sets the value of the '{@link org.archstudio.prolog.xtext.prolog.Expression#getString <em>String</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>String</em>' attribute.
   * @see #getString()
   * @generated
   */
  void setString(String value);

  /**
   * Returns the value of the '<em><b>Number</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Number</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Number</em>' attribute.
   * @see #setNumber(String)
   * @see org.archstudio.prolog.xtext.prolog.PrologPackage#getExpression_Number()
   * @model
   * @generated
   */
  String getNumber();

  /**
   * Sets the value of the '{@link org.archstudio.prolog.xtext.prolog.Expression#getNumber <em>Number</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Number</em>' attribute.
   * @see #getNumber()
   * @generated
   */
  void setNumber(String value);

  /**
   * Returns the value of the '<em><b>List</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>List</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>List</em>' attribute.
   * @see #setList(boolean)
   * @see org.archstudio.prolog.xtext.prolog.PrologPackage#getExpression_List()
   * @model
   * @generated
   */
  boolean isList();

  /**
   * Sets the value of the '{@link org.archstudio.prolog.xtext.prolog.Expression#isList <em>List</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>List</em>' attribute.
   * @see #isList()
   * @generated
   */
  void setList(boolean value);

  /**
   * Returns the value of the '<em><b>Head</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Head</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Head</em>' containment reference.
   * @see #setHead(Expression)
   * @see org.archstudio.prolog.xtext.prolog.PrologPackage#getExpression_Head()
   * @model containment="true"
   * @generated
   */
  Expression getHead();

  /**
   * Sets the value of the '{@link org.archstudio.prolog.xtext.prolog.Expression#getHead <em>Head</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Head</em>' containment reference.
   * @see #getHead()
   * @generated
   */
  void setHead(Expression value);

  /**
   * Returns the value of the '<em><b>Tail</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Tail</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Tail</em>' containment reference.
   * @see #setTail(Expression)
   * @see org.archstudio.prolog.xtext.prolog.PrologPackage#getExpression_Tail()
   * @model containment="true"
   * @generated
   */
  Expression getTail();

  /**
   * Sets the value of the '{@link org.archstudio.prolog.xtext.prolog.Expression#getTail <em>Tail</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Tail</em>' containment reference.
   * @see #getTail()
   * @generated
   */
  void setTail(Expression value);

  /**
   * Returns the value of the '<em><b>Paren</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Paren</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Paren</em>' attribute.
   * @see #setParen(boolean)
   * @see org.archstudio.prolog.xtext.prolog.PrologPackage#getExpression_Paren()
   * @model
   * @generated
   */
  boolean isParen();

  /**
   * Sets the value of the '{@link org.archstudio.prolog.xtext.prolog.Expression#isParen <em>Paren</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Paren</em>' attribute.
   * @see #isParen()
   * @generated
   */
  void setParen(boolean value);

  /**
   * Returns the value of the '<em><b>Sub</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Sub</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sub</em>' containment reference.
   * @see #setSub(Expression)
   * @see org.archstudio.prolog.xtext.prolog.PrologPackage#getExpression_Sub()
   * @model containment="true"
   * @generated
   */
  Expression getSub();

  /**
   * Sets the value of the '{@link org.archstudio.prolog.xtext.prolog.Expression#getSub <em>Sub</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sub</em>' containment reference.
   * @see #getSub()
   * @generated
   */
  void setSub(Expression value);

} // Expression
