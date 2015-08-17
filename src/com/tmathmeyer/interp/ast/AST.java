package com.tmathmeyer.interp.ast;

import java.util.List;

import com.tmathmeyer.interp.expr.Function.Pair;
import com.tmathmeyer.interp.macro.Macro;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.values.EmptyList;
import com.tmathmeyer.interp.values.ImmutableList;

public interface AST
{
    Expression asExpression();

    String name();

    AST toRepeatingTree();

    ImmutableList<ASTBinding> structureCompare(AST t) throws MismatchedRepetitionSizeException;

    ImmutableList<ASTBinding> structureCompare(ASTree t) throws MismatchedRepetitionSizeException;

    ImmutableList<ASTBinding> structureCompare(ASNode t) throws MismatchedRepetitionSizeException;

    ImmutableList<ASTBinding> structureCompare(RepeatingAST t) throws MismatchedRepetitionSizeException;

    public interface ASTBinding
    {
        ASTBinding setRepeatingTag();

        boolean isRepeating();

        ImmutableList<ASTBinding> asList();
    }

    class ASTreeBinding implements ASTBinding
    {
        private final ImmutableList<ASTBinding> list;
        private final boolean repeating;

        private ASTreeBinding(ImmutableList<ASTBinding> ilast, boolean rep)
        {
            list = ilast;
            repeating = rep;
        }

        ASTreeBinding(ImmutableList<ASTBinding> ilast)
        {
            this(ilast, false);
        }

        @Override
        public String toString()
        {
            return "binds=[" + list + "]";
        }

        @Override
        public ASTBinding setRepeatingTag()
        {
            return new ASTreeBinding(list, true);
        }

        @Override
        public boolean isRepeating()
        {
            return repeating;
        }

        @Override
        public ImmutableList<ASTBinding> asList()
        {
            return list;
        }
    }

    class ASNodeBinding implements ASTBinding
    {
        private final ASNode from;
        private final AST to;
        private final boolean repeating;

        private ASNodeBinding(ASNode node, AST t, boolean rep)
        {
            from = node;
            to = t;
            repeating = rep;
        }

        ASNodeBinding(ASNode node, AST t)
        {
            this(node, t, false);
        }

        @Override
        public String toString()
        {
            return "(" + from + "###" + to + ")";
        }

        @Override
        public ASTBinding setRepeatingTag()
        {
            return new ASNodeBinding(from, to, true);
        }

        @Override
        public boolean isRepeating()
        {
            return repeating;
        }

        public ASNode getFrom()
        {
            return from;
        }

        public AST getTo()
        {
            return to;
        }

        @Override
        public ImmutableList<ASTBinding> asList()
        {
            return new EmptyList<ASTBinding>().add(this);
        }
    }

    ASTBinding bindTo(AST bNext) throws MismatchedRepetitionSizeException;

    List<AST> getParts();

    AST applyBindings(ImmutableList<ASTBinding> comp);

    Pair<AST, Boolean> applyMacro(Macro macro);

    AST hasMacro(String name);

    boolean isMacro();
}
