let stdio =
    include
        """"#include <stddef.h>

            #define SEEK_SET    0
            #define SEEK_CUR    1
            #define SEEK_END    2
            typedef struct _IO_FILE FILE;

            FILE *fopen(const char *pathname, const char *mode);
            size_t fread(void *ptr, size_t size, size_t nmemb, FILE *stream);
            size_t fwrite(const void *ptr, size_t size, size_t nmemb, FILE *stream);
            int fclose(FILE *stream);
            int fseek(FILE *stream, long offset, int whence);
            long ftell(FILE *stream);
            void rewind(FILE *stream);

            int printf(const char *restrict format, ...);
            int fprintf(FILE *restrict stream, const char *restrict format, ...);

..
    stdio.extern
    stdio.define
