const CleanWebpackPlugin = require('clean-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const UglifyJsWebpackPlugin = require('uglifyjs-webpack-plugin');
const ExtractTextWebpackPlugin = require('extract-text-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');

module.exports = {
    entry: {
        index: './src/mobile/index.tsx',
        order: './src/mobile/order.tsx',
        mine: './src/mobile/mine.tsx',
        mobileSignIn: './src/mobile/sign-in.tsx',
        console: './src/console/index.tsx',
        consoleSignIn: './src/console/sign-in.tsx'
    },

    output: {
        filename: 'js/[chunkhash:8].js',
        path: __dirname + '/dist'
    },

    plugins: [
        new CleanWebpackPlugin(['dist']),
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: './src/template/index.html',
            chunks: ['index'],
            minify: {
                collapseWhitespace: true
            },
            title: 'Ranch UI'
        }),
        new HtmlWebpackPlugin({
            filename: 'order.html',
            template: './src/template/index.html',
            chunks: ['order'],
            minify: {
                collapseWhitespace: true
            },
            title: 'Ranch UI'
        }),
        new HtmlWebpackPlugin({
            filename: 'mine.html',
            template: './src/template/index.html',
            chunks: ['mine'],
            minify: {
                collapseWhitespace: true
            },
            title: 'Ranch UI'
        }),
        new HtmlWebpackPlugin({
            filename: 'mobile-sign-in.html',
            template: './src/template/index.html',
            chunks: ['mobileSignIn'],
            minify: {
                collapseWhitespace: true
            },
            title: '请登陆'
        }),
        new HtmlWebpackPlugin({
            filename: 'console.html',
            template: './src/template/index.html',
            chunks: ['console'],
            minify: {
                collapseWhitespace: true
            },
            title: 'Ranch UI'
        }),
        new HtmlWebpackPlugin({
            filename: 'console-sign-in.html',
            template: './src/template/index.html',
            chunks: ['consoleSignIn'],
            minify: {
                collapseWhitespace: true
            },
            title: '请登陆'
        }),
        new UglifyJsWebpackPlugin(),
        new ExtractTextWebpackPlugin({
            filename: (getPath) => {
                return getPath('css/[contenthash:8].css');
            }
        }),
        new CopyWebpackPlugin([{
            from: './src/template/css/*',
            to: 'css',
            flatten: true
        }, {
            from: './src/template/icon/*',
            to: 'icon',
            flatten: true
        }, {
            from: './src/template/img/*',
            to: 'img',
            flatten: true
        }])
    ],

    resolve: {
        extensions: ['.ts', '.tsx', '.js', '.json']
    },

    module: {
        rules: [{
                test: /\.tsx?$/,
                loader: 'awesome-typescript-loader',
                options: {
                    minimize: true
                }
            },
            {
                test: /\.(less)$/,
                use: ExtractTextWebpackPlugin.extract({
                    fallback: 'style-loader',
                    use: [{
                        loader: 'css-loader',
                        options: {
                            minimize: true
                        }
                    }, 'less-loader']
                })
            },
            {
                test: /\.(css)$/,
                use: ExtractTextWebpackPlugin.extract({
                    fallback: 'style-loader',
                    use: {
                        loader: 'css-loader',
                        options: {
                            minimize: true
                        }
                    }
                })
            },
            {
                test: /\.(png)|(jpg)|(jpeg)|(svg)$/,
                loader: 'file-loader',
                options: {
                    publicPath: '../',
                    outputPath: 'img/',
                    name: '[hash:8].[ext]'
                }
            }
        ]
    }
};