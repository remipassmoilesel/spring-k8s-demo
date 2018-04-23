const CommonsChunkPlugin = require('webpack/lib/optimize/CommonsChunkPlugin')
const UglifyJsPlugin = require('webpack/lib/optimize/UglifyJsPlugin')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const CompressionPlugin = require('compression-webpack-plugin')
const ExtractTextPlugin = require('extract-text-webpack-plugin')
const webpackConfig = require('./webpack.config.base')
const helpers = require('./helpers')
const OptimizeCssAssetsPlugin = require('optimize-css-assets-webpack-plugin')

const extractSass = new ExtractTextPlugin({
  filename: 'css/[name].css',
  disable: process.env.NODE_ENV === 'development'
})

webpackConfig.module.rules = [...webpackConfig.module.rules,
  {
    test: /\.s?css$/,
    use: extractSass.extract({
      use: [{
        loader: 'css-loader',
        options: {
          minimize: false,
          sourceMap: false,
          importLoaders: 2
        }
      },
      {
        loader: 'sass-loader',
        options: {
          sourceMap: false
        }
      }
      ],
      // use style-loader in development
      fallback: 'style-loader'
    })
  },
  {
    test: /\.(jpg|png|gif)$/,
    loader: 'file-loader',
    options: {
      regExp: /(img\/.*)/,
      name: '[name].[ext]',
      publicPath: '/assets/img/',
      outputPath: 'assets/img/'
    }
  },
  {
    test: /\.(eot|svg|ttf|woff|woff2)$/,
    loader: 'file-loader',
    options: {
      regExp: /(fonts\/.*)/,
      name: '[name].[ext]',
      publicPath: '/assets/fonts/',
      outputPath: 'assets/fonts/'
    }
  }
]

// TODO: restore
// ensure ts lint fails the build
// webpackConfig.module.rules[0].options = {
//   failOnHint: true
// }

webpackConfig.plugins = [...webpackConfig.plugins,
  new CommonsChunkPlugin({
    name: 'vendor',
    minChunks: function (module) {
      return module.context && module.context.indexOf('node_modules') !== -1
    }
  }),
  new CommonsChunkPlugin({
    name: 'manifest',
    minChunks: Infinity
  }),
  extractSass,
  new OptimizeCssAssetsPlugin({
    cssProcessor: require('cssnano'),
    cssProcessorOptions: {
      discardUnused: false,
      discardComments: { removeAll: true }
    },
    canPrint: true
  }),
  new HtmlWebpackPlugin({
    inject: true,
    template: helpers.root('/src/index-dev.html'),
    minify: {
      removeComments: true,
      collapseWhitespace: true,
      removeRedundantAttributes: true,
      useShortDoctype: true,
      removeEmptyAttributes: true,
      removeStyleLinkTypeAttributes: true,
      keepClosingSlash: true,
      minifyJS: true,
      minifyCSS: true,
      minifyURLs: true
    }
  }),
  new UglifyJsPlugin({
    include: /\.js$/,
    minimize: true
  }),
  new CompressionPlugin({
    asset: '[path].gz[query]',
    test: /\.js$/
  }),
]

module.exports = webpackConfig
