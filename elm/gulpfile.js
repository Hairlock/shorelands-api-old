var gulp = require('gulp');
var elm  = require('gulp-elm');
var gutil = require('gulp-util');
var plumber = require('gulp-plumber');
var del = require('del');
var notify = require('gulp-notify');
var through = require('gulp-through');
var livereload = require('gulp-livereload');


gulp.task('elm-init', elm.init);

gulp.task('clean', function(){
    return del([
        'resources/public/**/*.js'
    ]);
});

gulp.task('elm', ['elm-init'], function(){
    return gulp.src('modules/**/*.elm')
        .pipe(plumber({
            errorHandler: notify.onError(function(error){
                gutil.log(error.toString());
                return "Elm Compile Error";
            })
        }))
        .pipe(plumber({
            errorHandler: function(e){
                gutil.log(e.toString());
            }
        }))
        .pipe(elm())
        .pipe(gulp.dest('../resources/public/js/'))
        .pipe(livereload());
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
});

gulp.task('default', ['elm', 'watch']);
