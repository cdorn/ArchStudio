/**
 */
package org.archstudio.prolog.xtext.prolog.impl;

import org.archstudio.prolog.xtext.prolog.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PrologFactoryImpl extends EFactoryImpl implements PrologFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static PrologFactory init()
  {
    try
    {
      PrologFactory thePrologFactory = (PrologFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.archstudio.org/prolog/xtext/Prolog"); 
      if (thePrologFactory != null)
      {
        return thePrologFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new PrologFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PrologFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case PrologPackage.PROGRAM: return createProgram();
      case PrologPackage.SINGLE_CLAUSE: return createSingleClause();
      case PrologPackage.QUERY: return createQuery();
      case PrologPackage.PREDICATE: return createPredicate();
      case PrologPackage.SINGLE_TERM: return createSingleTerm();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Program createProgram()
  {
    ProgramImpl program = new ProgramImpl();
    return program;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SingleClause createSingleClause()
  {
    SingleClauseImpl singleClause = new SingleClauseImpl();
    return singleClause;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Query createQuery()
  {
    QueryImpl query = new QueryImpl();
    return query;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Predicate createPredicate()
  {
    PredicateImpl predicate = new PredicateImpl();
    return predicate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SingleTerm createSingleTerm()
  {
    SingleTermImpl singleTerm = new SingleTermImpl();
    return singleTerm;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PrologPackage getPrologPackage()
  {
    return (PrologPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static PrologPackage getPackage()
  {
    return PrologPackage.eINSTANCE;
  }

} //PrologFactoryImpl
