
// Defines
#define SPL_JACK 1
#define SPL_JANE 0

// typedef
typedef struct SplThing
{
  int a;
  int b;
} SplThing;

// function pointer
typedef void (* SplFunc)(int a, int b);

// functions
int spl_foo(int a);


int spl_bar(int b);

