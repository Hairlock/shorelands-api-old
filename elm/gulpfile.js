var gulp = require('gulp');
var elm  = require('gulp-elm');
var gutil = require('gulp-util');
var plumber = require('gulp-plumber');
var notify = require('gulp-notify');
var clean = require('gulp-clean');
var concat = require('gulp-concat');
var livereload = require('gulp-livereload');


gulp.task('elm-init', elm.init);

gulp.task('elm', ['elm-init'], function(){
    return gulp.src('modules/**/*.elm')
        .pipe(plumber({
            errorHandler: notify.onError(function(error){
                gutil.log(error.toString());
                return "Elm Compile Error";
            })
        }))
        .pipe(elm())
        .pipe(gulp.dest('../resources/public/js/'))
        .pipe(livereload());
});

gulp.task('clean-sql', function(){
    return gulp.src('../resources/sql/build/bundle.sql', {read: false})
        .pipe(clean({force: true}));
});

gulp.task('sql', ['clean-sql'], function(){
    return gulp.src('../resources/sql/src/**/*.sql')
            .pipe(concat('bundle.sql', {newLine: '\n\n\n'}))
            .pipe(gulp.dest('../resources/sql/build'));
});

gulp.task('html', function(){
    return gulp.src('../resources/templates/**/*.html')
        .pipe(livereload());
});

// watch for changes
gulp.task('watch', function() {
    livereload.listen();
    gulp.watch('**/*.elm', ['elm']);
    //gulp.watch('resources/templates/**/*.html', ['html'])
    gulp.watch('../resources/templates/**/*.html').on('change', livereload.changed);

    gulp.watch('../resources/sql/src/**/*.sql', ['sql']);
});

gulp.task('default', ['elm', 'watch']);
