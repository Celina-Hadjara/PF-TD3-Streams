package fr.exercice1;

public class Paire<A,B> {
    private A _fst;
    private B _snd;

    public Paire(final A fst, final B snd) {
        _fst = fst;
        _snd = snd;
    }

    public Paire() {

    }

    public A fst() { return _fst; }

    public B snd() { return _snd; }

    @Override
    public String toString() {
        if(_fst == null) return String.format("(%s, %s) :: Paire[%s,%s]", "null_DEF", _snd, "null_DEF", _snd.getClass().getSimpleName());
        else return String.format("(%s, %s) :: Paire[%s,%s]", _fst, _snd, _fst.getClass().getSimpleName(), _snd.getClass().getSimpleName()); }

    public <C> Paire<C,B> changeFst(C val) { return new Paire<>(val, _snd); }

    public <C> Paire<A,C> changeSnd(C val) { return new Paire<>(_fst, val); }
}
