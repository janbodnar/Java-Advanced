// The == operator compares object references and equals()
// object values. Java internally caches integers 
// from -128 to +127, so comparing them with == 'works'.
// we should use equals

void main() {

    Integer a = 127;
    Integer b = 127;
    Integer c = 128;
    Integer d = 128;

    IO.println(a == b);
    IO.println(c == d);
}

